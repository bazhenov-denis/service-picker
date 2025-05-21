INSERT INTO offers (
    product_id, tariff, code,
    child_code_1, child_count_1,
    period, region_id, profrole_group_id,
    price_all, currency
) VALUES
(907283, 'DI', 'CIV', '1280', 12800, 30, 1620, 0, 15693600, 'RUR'),
(907287, 'DI', 'CIV', '3200', 32000, 92, 1624, 0, 38624600, 'RUR'),
(907304, 'DI', 'CIV', '700', 7000, 30, 1530, 0, 8512000, 'RUR'),
(907308, 'DI', 'CIV', '700', 7000, 30, 1620, 0, 7256500, 'RUR');


INSERT INTO region_area_mapping (id, price_region_id, area_id) VALUES
(1, 1, 77),
(2, 1, 78),
(3, 2, 66);


INSERT INTO price_profrole (id, name) VALUES
(1, 'IT-специалисты'),
(2, 'Маркетинг и продажи');


INSERT INTO profrole (id, name) VALUES
(1, 'Backend-разработчик'),
(2, 'Frontend-разработчик'),
(3, 'Маркетолог');


INSERT INTO profrole_group_mapping (id, price_profrole_id, profrole_id) VALUES
(1, 1, 1),
(2, 1, 2),
(3, 2, 3);
