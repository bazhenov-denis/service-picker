DROP TABLE option_scores;

DROP TABLE options;

DROP TABLE questions;

CREATE TABLE questions (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    question_text TEXT NOT NULL,
    reference_type VARCHAR(50),
    is_required BOOLEAN NOT NULL,
    short_title VARCHAR(255),
    position INT,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE options (
    id          SERIAL PRIMARY KEY,
    question_id INT  NOT NULL,
    text        TEXT NOT NULL,
    CONSTRAINT fk_options_question
        FOREIGN KEY (question_id)
            REFERENCES questions (id)
            ON DELETE CASCADE,
    position INT
);

CREATE TABLE option_scores
(
    id            SERIAL PRIMARY KEY,
    option_id     INT           NOT NULL,
    score_type_id INT           NOT NULL,
    weight        NUMERIC(5, 2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_option_scores_option
        FOREIGN KEY (option_id)
            REFERENCES options (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_option_scores_score_type
        FOREIGN KEY (score_type_id)
            REFERENCES score_types (id)
            ON DELETE CASCADE
);

INSERT INTO questions (type, question_text, reference_type, is_required,
                       short_title, position)
VALUES ('reference', 'Выберите регион:', 'regions', TRUE,
        'Регион:', 1),
       ('reference', 'Выберите профессию:', 'professions', TRUE,
        'Профессия', 2),
       ('input', 'Сколько человек необходимо?', NULL, TRUE,
        'Количество', 3),
       ('single-choice', 'Как срочно нужен человек?', NULL, FALSE,
        'Срочность', 4)
;

INSERT INTO options (question_id, text, position)
VALUES (4, 'как можно скорее', 1),
       (4, 'ближайшая неделя', 2),
       (4, 'ближайший месяц', 3),
       (4, 'не срочно', 4);

INSERT INTO questions (type, question_text, reference_type, is_required,
                       short_title, position)
VALUES ('single-choice', 'Как часто вы ищете сотрудников?', 'hiring_frequency', FALSE,
        'Частота найма:', 5),
       ('single-choice', 'Есть ли у вас время на самостоятельный поиск?:', 'search_time', FALSE,
        'Время на поиск', 6)
;

INSERT INTO options (question_id, text, position)
VALUES (5, 'разово', 1),
       (5, 'сезонный подбор', 2),
       (5, 'постоянный поиск', 3),
       (5, 'расширение штата', 4);

INSERT INTO options (question_id, text, position)
VALUES (6, 'да', 1),
       (6, 'немного', 2),
       (6, 'нет', 3),
       (6, 'затрудняюсь ответить', 4);

INSERT INTO questions (
    type, question_text, reference_type,
    is_required, short_title, position, active
) VALUES (
             'single-choice',
             'Насколько важен опыт работы кандидата?',
             'experience_importance',
             FALSE,
             'Важность опыта',
             7,
             FALSE
         );

INSERT INTO options (question_id, text, position)
VALUES
    (7, 'Критически важен',    1),
    (7, 'Желателен',           2),
    (7, 'Не имеет значения',    3);

-- Добавляем новые связи
INSERT INTO option_scores (option_id, score_type_id, weight)
VALUES
-- options 1–4 → срочность (score_type_id = 4)
(1, 4,  10),
(2, 4,   5),
(3, 4,   0),
(4, 4,  -5),
-- options 5–8 → постоянность (score_type_id = 6)
(5, 6,  -5),
(6, 6,   1),
(7, 6,   5),
(8, 6,  10),
-- options 9–12 → доступ к резюме (score_type_id = 2)
(9, 2,  10),
(10, 2,   5),
(11, 2,   -5),
(12, 2,  3);

INSERT INTO option_scores (option_id, score_type_id, weight)
VALUES
    (13, 3, 10.00),  -- +10 к конкурентности
    (14, 3,  5.00),  -- +5 к конкурентности
    (15, 3,  0.00);  -- +0 к конкурентности
