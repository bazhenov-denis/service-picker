ALTER TABLE questions
    ADD COLUMN short_title VARCHAR(255),
    ADD COLUMN position    INT;

ALTER TABLE options
    ADD COLUMN position INT;

TRUNCATE TABLE options CASCADE;
TRUNCATE TABLE questions CASCADE;

-- 2) Вставляем заново, сразу со всеми новыми колонками
INSERT INTO questions (id, type, question_text, reference_type, is_required,
                       short_title, position)
VALUES (1, 'reference', 'Выберите регион:', 'regions', TRUE,
        'Регион:', 1),
       (2, 'reference', 'Выберите профессию:', 'professions', TRUE,
        'Профессия', 2),
       (3, 'input', 'Сколько человек необходимо?', NULL, TRUE,
        'Количество', 3),
       (4, 'single-choice', 'Как срочно нужен человек?', NULL, FALSE,
        'Срочность', 4)
;

INSERT INTO options (id, question_id, text, position)
VALUES (1, 4, 'как можно скорее', 1),
       (2, 4, 'ближайшая неделя', 2),
       (3, 4, 'ближайший месяц', 3),
       (4, 4, 'не срочно', 4);
