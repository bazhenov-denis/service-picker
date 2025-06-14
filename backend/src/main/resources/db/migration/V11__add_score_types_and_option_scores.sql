CREATE TABLE score_types
(
    id    INT PRIMARY KEY,
    code  VARCHAR(50)  NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL
);

CREATE TABLE option_scores
(
    id            INT PRIMARY KEY,
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

INSERT INTO score_types (id, code, title)
VALUES (1, 'access_vacancies', 'Доступ к вакансиям'),
       (2, 'access_resumes', 'Доступ к резюме'),
       (3, 'competition', 'Конкурентность'),
       (4, 'urgency', 'Срочность'),
       (5, 'mass', 'Массовость'),
       (6, 'region', 'Регион'),
       (7, 'profession', 'Профессия');

