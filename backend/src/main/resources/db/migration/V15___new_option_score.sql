DELETE FROM option_scores;

-- Добавляем новые связи
INSERT INTO option_scores (id, option_id, score_type_id, weight)
VALUES
-- options 1–4 → срочность (score_type_id = 4)
(1,  1, 4,  10),
(2,  2, 4,   5),
(3,  3, 4,   0),
(4,  4, 4,  -5),
-- options 5–8 → постоянность (score_type_id = 6)
(5,  5, 6,  -5),
(6,  6, 6,   1),
(7,  7, 6,   5),
(8,  8, 6,  10),
-- options 9–12 → доступ к резюме (score_type_id = 2)
(9,  9, 2,  10),
(10, 10, 2,   5),
(11, 11, 2,   -5),
(12, 12, 2,  3);
