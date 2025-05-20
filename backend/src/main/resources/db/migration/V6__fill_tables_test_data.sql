INSERT INTO offers (
    id, product_id, tariff, code,
    child_code_1, child_count_1,
    child_code_2, child_count_2,
    child_code_3, child_count_3,
    child_code_4, child_count_4,
    period, region_id, profrole_group_id,
    price_all, currency
) VALUES
(1, 101, 'Standard', 'OFFER1', 'CH1', 2, 'CH2', 1, 'CH3', 1, 'CH4', 2, 12, 1, 1, 4999.99, 'RUB'),
(2, 102, 'Premium',  'OFFER2', 'CH3', 3, 'CH4', 2, 'CH5', 1, 'CH6', 1, 6,  2, 2, 8999.00, 'RUB'),
(3, 103, 'Basic',    'OFFER3', 'CH7', 1, 'CH8', 2, 'CH9', 1, 'CH10', 1, 1,  1, 1, 1999.99, 'RUB');


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
