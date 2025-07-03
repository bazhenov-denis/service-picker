import aiohttp
import asyncio
import os

from dotenv import load_dotenv
from aiogram import Bot, Dispatcher, types, F
from aiogram.client.default import DefaultBotProperties
from aiogram.fsm.storage.memory import MemoryStorage
from aiogram.fsm.context import FSMContext
from aiogram.fsm.state import StatesGroup, State
from aiogram.filters.state import StateFilter
from aiogram.utils.keyboard import InlineKeyboardBuilder
from aiogram.filters import Command
from aiogram.types import CallbackQuery, InlineKeyboardMarkup, InlineKeyboardButton


class Survey(StatesGroup):
    waiting_for_region = State()
    waiting_for_city = State()
    waiting_for_answer = State()
    waiting_for_prof_category = State()
    waiting_for_prof_role = State()


class TelegramBot:
    def __init__(self):
        load_dotenv(override=True)
        self.storage = MemoryStorage()
        self.bot = Bot(token=os.getenv("TG_TOKEN"), default=DefaultBotProperties(parse_mode="HTML"))
        self.dp = Dispatcher(storage=self.storage)
        self.host = os.getenv("HOST")
        self.service_number = int(os.getenv("SERV_NUMBER"))

    async def register_handlers(self):
        self.dp.message.register(self.cmd_start, Command(commands=["start"]))
        self.dp.message.register(self.handle_text_response, F.text)
        self.dp.callback_query.register(self.handle_query, Survey.waiting_for_answer)
        self.dp.callback_query.register(self.handle_query, Survey.waiting_for_prof_role)
        self.dp.callback_query.register(self.on_region_selected, F.data.startswith("region:"), Survey.waiting_for_region)
        self.dp.callback_query.register(self.on_city_selected, F.data.startswith("city:"), Survey.waiting_for_city)
        self.dp.callback_query.register(self.on_prof_category_selected, F.data.startswith("prof_cat:"), Survey.waiting_for_prof_category)

    async def cmd_start(self, message: types.Message, state: FSMContext):
        await message.answer("Привет, это бот для подбора оптимальных услуг hh.")
        areas_data = await self.get_areas_data()
        await state.update_data(areas_data=areas_data)

        keyboard = self.build_regions_keyboard(areas_data)
        await message.answer("Выберите регион:", reply_markup=keyboard)
        await state.set_state(Survey.waiting_for_region)

    async def get_areas_data(self):
        url = f"{self.host}/areas"
        async with aiohttp.ClientSession() as session:
            async with session.get(url) as response:
                return await response.json()

    def build_regions_keyboard(self, areas_data):
        regions = next(item for item in areas_data if item["id"] == "113")["areas"]
        kb = InlineKeyboardMarkup(inline_keyboard=[
            [InlineKeyboardButton(text=region["name"], callback_data=f"region:{region['id']}")]
            for region in regions
        ])
        return kb

    def build_cities_keyboard(self, region_id: str, areas_data):
        regions = next(item for item in areas_data if item["id"] == "113")["areas"]
        selected_region = next(region for region in regions if region["id"] == region_id)
        cities = selected_region["areas"]
        kb = InlineKeyboardMarkup(inline_keyboard=[
            [InlineKeyboardButton(text=city["name"], callback_data=f"city:{city['id']}")]
            for city in cities
        ])
        return kb

    async def on_region_selected(self, call: CallbackQuery, state: FSMContext):
        await call.answer()
        region_id = call.data.split(":")[1]
        data = await state.get_data()
        areas_data = data.get("areas_data")

        if not areas_data:
            areas_data = await self.get_areas_data()
            await state.update_data(areas_data=areas_data)

        russia = next((item for item in areas_data if item["id"] == "113"), None)
        if not russia:
            await call.message.edit_text("Ошибка: страна не найдена.")
            return

        selected_region = next((region for region in russia["areas"] if region["id"] == region_id), None)
        if not selected_region:
            await call.message.edit_text("Регион не найден.")
            return

        cities = selected_region.get("areas", [])
        if not cities:
            await call.message.edit_text("В этом регионе нет городов.")
            return

        keyboard = self.build_cities_keyboard(region_id, areas_data)
        await call.message.edit_text("Выберите город:", reply_markup=keyboard)
        await state.update_data(selected_region=region_id)
        await state.set_state(Survey.waiting_for_city)

    async def on_city_selected(self, call: CallbackQuery, state: FSMContext):
        await call.answer()
        city_id = call.data.split(":")[1]

        data = await state.get_data()
        region_id = data.get("selected_region")
        areas_data = data.get("areas_data")

        regions = next(item for item in areas_data if item["id"] == "113")["areas"]
        selected_region = next(region for region in regions if region["id"] == region_id)
        city = next(city for city in selected_region["areas"] if city["id"] == city_id)
        city_name = city["name"]

        await call.message.edit_reply_markup(reply_markup=None)
        await call.message.answer(f"Вы выбрали город: {city_name}")

        questions = await self.fetch_questions()

        await state.update_data(
            selected_city=city_id,
            selected_region=region_id,
            questions=questions,
            current_question=0,
            answers=[]
        )

        await self.ask_next_question(call.message, state)

    async def handle_query(self, call: CallbackQuery, state: FSMContext):
        await call.answer()
        data = await state.get_data()
        index = data.get("current_question", 0)
        answers = data.get("answers", [])
        questions = data.get("questions", [])

        chosen_text = call.message.reply_markup.inline_keyboard
        selected_text = None
        for row in chosen_text:
            for button in row:
                if button.callback_data == call.data:
                    selected_text = button.text
                    break

        if call.data.startswith("ref:"):
            _, ref_type, ref_id = call.data.split(":")
            answers.append(ref_id)
        else:
            answers.append(call.data)

        index += 1
        await state.update_data(answers=answers, current_question=index)

        await call.message.edit_reply_markup(reply_markup=None)
        if selected_text:
            await call.message.answer(f"Вы выбрали: {selected_text}")

        if index >= len(questions):
            await call.message.answer("Начало подбора услуг...")

            payload = {str(i + 1): [str(ans)] for i, ans in enumerate(answers)}
            region_id = data.get("selected_region")
            city_id = data.get("selected_city")
            if region_id and city_id:
                payload["1"] = [f"113.{region_id}.{city_id}"]

            async with aiohttp.ClientSession() as session:
                async with session.post(f"{self.host}/service-offer", json=payload) as resp:
                    if resp.status == 200:
                        result = await resp.json()
                        result = result['items'][:self.service_number]
                        print(result)
                        response = f"Я подобрал {self.service_number} наиболее подходящих услуг:\n\n"
                        for res in result:
                            response += f"<b>{res['label']}: {res['title']}</b> \nПериод: {res['period']}\nГруппа профессий: {res['profroleGroup']}\nЦена: {res['price']}\nКоличество контактов: {res['civCount']}\nЛимит API: {res['apiLimitedCount']}\n\n"
                        await call.message.answer(response, parse_mode='HTML')
                    else:
                        await call.message.answer(f"Ошибка от сервера: {resp.status}")

            keyboard = InlineKeyboardMarkup(
                inline_keyboard=[[InlineKeyboardButton(text="Искать заново", callback_data="restart_search")]])

            await call.message.answer("Хотите поискать заново?", reply_markup=keyboard)

            await state.clear()
            return

        await self.ask_next_question(call.message, state)

    async def handle_text_response(self, message: types.Message, state: FSMContext):
        data = await state.get_data()
        index = data.get("current_question", 0)
        answers = data.get("answers", [])
        questions = data.get("questions", [])

        text = message.text.strip()

        if not text.isdigit():
            await message.answer("Введён некорректный ответ. Либо выберите ответ на клавиатуре выше, либо введите число для вопросов без клавиатуры.")
            return

        question = questions[index]
        question_type = question.get("type")
        options = question.get("options", [])

        if question_type == "input":
            if not text:
                await message.answer("Ответ не может быть пустым. Пожалуйста, введите ответ.")
                return

            answers.append(text)
            index += 1
            await state.update_data(answers=answers, current_question=index)
            await self.ask_next_question(message, state)
            return

        if text not in [option.get("text") for option in options]:
            await message.answer("Пожалуйста, выберите вариант из кнопок, а не вводите текст.")
            return

        answers.append(text if text else " ")
        index += 1
        await state.update_data(answers=answers, current_question=index)
        await self.ask_next_question(message, state)

    async def ask_next_question(self, message: types.Message, state: FSMContext):
        data = await state.get_data()
        questions = data.get("questions", [])
        index = data.get("current_question", 0)
        answers = data.get("answers", [])

        if index >= len(questions):
            return

        question = questions[index]
        question_text = question.get("questionText", "Вопрос без текста")
        question_type = question.get("type")
        reference_type = question.get("referenceType")
        options = question.get("options")

        if not options and reference_type:
            options = await self.fetch_reference(reference_type)
            question["options"] = options

        try:
            if question_type == "reference":
                if reference_type == "regions":
                    await state.update_data(current_question=index + 1)
                    await self.ask_next_question(message, state)
                    return

                if reference_type == "professions":
                    professions_data = await self.get_professions()
                    await state.update_data(professions_data=professions_data, current_question=index)
                    builder = InlineKeyboardBuilder()
                    for category in professions_data.get("categories", []):
                        builder.row(
                            InlineKeyboardButton(
                                text=category["name"],
                                callback_data=f"prof_cat:{category['id']}"
                            )
                        )
                    await self.bot.send_message(
                        chat_id=message.chat.id,
                        text="Выберите категорию профессии:",
                        reply_markup=builder.as_markup()
                    )
                    await state.set_state(Survey.waiting_for_prof_category)
                    return

                if not options:
                    await message.answer(f"Нет вариантов для {reference_type}")
                    await state.update_data(current_question=index + 1)
                    await self.ask_next_question(message, state)
                    return

                builder = InlineKeyboardBuilder()
                for variant in options:
                    text = variant.get("text") or variant.get("name") or str(variant)
                    callback_data = str(variant.get("id") or text)
                    builder.row(InlineKeyboardButton(text=text, callback_data=f"ref:{reference_type}:{callback_data}"))

                await self.bot.send_message(
                    chat_id=message.chat.id,
                    text=question_text,
                    reply_markup=builder.as_markup()
                )
                await state.set_state(Survey.waiting_for_answer)
                return

            elif question_type == "input":
                await self.bot.send_message(chat_id=message.chat.id, text=question_text)
                await state.set_state(Survey.waiting_for_answer)
                return

            elif question_type == "single-choice":
                if not options:
                    await message.answer("Нет доступных вариантов")
                    await state.update_data(current_question=index + 1)
                    await self.ask_next_question(message, state)
                    return

                builder = InlineKeyboardBuilder()
                for opt in options:
                    builder.row(
                        InlineKeyboardButton(text=opt["text"], callback_data=str(opt["id"]))
                    )
                await self.bot.send_message(
                    chat_id=message.chat.id,
                    text=question_text,
                    reply_markup=builder.as_markup()
                )
                await state.set_state(Survey.waiting_for_answer)
                return

            else:
                await message.answer(f"Неизвестный тип вопроса {question_type}")

        except Exception as e:
            await message.answer(f"Ошибка при показе вопроса  {e}")

    async def fetch_reference(self, reference_type: str):
        url = f"{self.host}/{reference_type}"
        try:
            async with aiohttp.ClientSession() as session:
                async with session.get(url) as response:
                    if response.status == 200:
                        result = await response.json()
                        if isinstance(result, list) and all(isinstance(x, str) for x in result):
                            return [{"id": i + 1, "text": x} for i, x in enumerate(result)]
                        return result
        except Exception as e:
            print(f"Ошибка загрузки {reference_type}:", e)
        return []

    async def get_professions(self):
        url = f"{self.host}/professions"
        async with aiohttp.ClientSession() as session:
            async with session.get(url) as response:
                return await response.json()

    async def fetch_questions(self):
        url = f"{self.host}/questions"
        async with aiohttp.ClientSession() as session:
            async with session.get(url) as response:
                if response.status == 200:
                    return await response.json()
                else:
                    raise Exception(f"Ошибка получения вопросов: {response.status}")

    async def on_prof_category_selected(self, call: CallbackQuery, state: FSMContext):
        await call.answer()
        cat_id = call.data.split(":")[1]
        data = await state.get_data()
        professions_data = data.get("professions_data", {})
        category = next((catt for catt in professions_data.get("categories", []) if catt["id"] == cat_id), None)

        if not category or not category.get("roles"):
            await call.message.edit_text("Нет профессий в выбранной категории.")
            return

        await state.set_state(Survey.waiting_for_prof_role)

        builder = InlineKeyboardBuilder()
        for role in category["roles"]:
            builder.row(
                InlineKeyboardButton(
                    text=role["name"],
                    callback_data=f"ref:professions:{role['id']}"
                )
            )

        await call.message.edit_text(
            f"Выберите профессию из категории: {category['name']}",
            reply_markup=builder.as_markup()
        )

    async def run(self):
        await self.register_handlers()
        try:
            await self.dp.start_polling(self.bot)
        except Exception as e:
            print("Ошибка:", e)

    async def handle_text(self, message, state=None):
        await self.bot.send_message(message.chat.id, "Используйте кнопки для навигации по боту.")
        await self.create_keyboard(message.chat.id)

if __name__ == "__main__":
    telegram_bot = TelegramBot()
    asyncio.run(telegram_bot.run())
