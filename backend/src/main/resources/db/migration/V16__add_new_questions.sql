INSERT INTO questions (
    id, type, question_text, reference_type,
    is_required, short_title, position, active
) VALUES (
             7,
             'single-choice',
             'Насколько важен опыт работы кандидата?',
             'experience_importance',
             FALSE,
             'Важность опыта',
             7,
             FALSE
         );

INSERT INTO options (id, question_id, text, position)
VALUES
    (13, 7, 'Критически важен',    1),
    (14, 7, 'Желателен',           2),
    (15, 7, 'Не имеет значения',    3);

INSERT INTO option_scores (id, option_id, score_type_id, weight)
VALUES
    (13,  13, 3, 10.00),  -- +10 к конкурентности
    (14,  14, 3,  5.00),  -- +5 к конкурентности
    (15,  15, 3,  0.00);  -- +0 к конкурентности
