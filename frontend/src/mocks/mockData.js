const mockData = {
  questions: [
    {
      id: 1,
      type: "reference",
      questionText: "Выберите регион:",
      referenceType: "regions",
      isRequired: true,
    },
    {
      id: 2,
      type: "reference",
      questionText: "Выберите профессию:",
      referenceType: "professions",
      isRequired: true,
    },
    {
      id: 3,
      type: "input",
      questionText: "Сколько человек необходимо?",
      inputType: "number",
      placeholder: "Введите число",
      validation: {
        min: 1,
        max: 100,
      },
      isRequired: true,
    },
    {
      id: 4,
      type: "single-choice",
      questionText: "Как срочно нужен человек",
      options: [
        { id: 1, text: "сегодня" },
        { id: 2, text: "1-2 недели" },
        { id: 3, text: "не срочно" },
      ],
      isRequired: false,
    },
    {
      id: 5,
      type: "multiple-choice",
      questionText: "Выберите услуги которые точно нужны:",
      options: [
        { id: 1, text: "Публикация вакансии" },
        { id: 2, text: "Доступ к базе резюме" },
        { id: 3, text: "Продвижение" },
      ],
      isRequired: false,
    },
  ],
};

export default mockData;
