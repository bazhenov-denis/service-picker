INSERT INTO profrole (id, name) VALUES
(4, 'Автомойщик'),
(8, 'Администратор');


INSERT INTO price_profrole (id, name) VALUES
(19, 'Автомобильный бизнес'),
(5, 'Административный персонал'),
(35, 'Рабочие'),
(37, 'Продажи, финансы, администрирование и HR');


INSERT INTO profrole_group_mapping (price_profrole_id, profrole_id) VALUES
(19, 4),
(35, 4),
(5, 8),
(37, 8); 
