INSERT INTO questions (id, type, question_text, reference_type, is_required)
VALUES (1, 'reference', 'Выберите регион:', 'regions', TRUE),
       (2, 'reference', 'Выберите профессию:', 'professions', TRUE),
       (3, 'input', 'Сколько человек необходимо?', NULL, TRUE),
       (4, 'single-choice', 'Как срочно нужен человек?', NULL, FALSE);

INSERT INTO options (id, question_id, text)
VALUES
    (1, 4, 'как можно скорее'),
    (2, 4, 'ближайшая неделя'),
    (3, 4, 'ближайший месяц'),
    (4, 4, 'не срочно');
