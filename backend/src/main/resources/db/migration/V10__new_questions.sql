INSERT INTO questions (id, type, question_text, reference_type, is_required,
                       short_title, position)
VALUES (5, 'single-choice', 'Как часто вы ищете сотрудников?', 'hiring_frequency', FALSE,
        'Частота найма:', 5),
       (6, 'single-choice', 'Есть ли у вас время на самостоятельный поиск?:', 'search_time', FALSE,
        'Время на поиск', 6)
;

INSERT INTO options (id, question_id, text, position)
VALUES (5, 5, 'разово', 1),
       (6, 5, 'сезонный подбор', 2),
       (7, 5, 'постоянный поиск', 3),
       (8, 5, 'расширение штата', 4);

INSERT INTO options (id, question_id, text, position)
VALUES (9, 6, 'да', 1),
       (10, 6, 'немного', 2),
       (11, 6, 'нет', 3),
       (12, 6, 'затрудняюсь ответить', 4);
