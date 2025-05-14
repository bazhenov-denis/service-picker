INSERT INTO questions (id, type, question_text, reference_type, is_required)
VALUES (1, 'reference', 'Выберите регион:', 'regions', TRUE),
       (2, 'reference', 'Выберите профессию:', 'professions', TRUE),
       (3, 'input', 'Сколько человек необходимо?', NULL, TRUE),
       (4, 'single-choice', 'Как срочно нужен человек?', NULL, FALSE);

INSERT INTO options (question_id, id, option_text)
VALUES
    (4, 1, 'как можно скорее'),
    (4, 2, 'ближайшие 2 недели'),
    (4, 3, 'ближайший месяц'),
    (4, 4, 'не срочно');
