DELETE
FROM area_mapping
WHERE price_region_id = 113
   OR area_id = 113;

DELETE
FROM price_region
WHERE id = 113;

DELETE
FROM area
WHERE id = 113;

--  (113 заменён на 1)
INSERT INTO area (id, name)
VALUES (1, 'Москва');

INSERT INTO price_region (id, name)
VALUES (1, 'Москва');


INSERT INTO area_mapping (price_region_id, area_id)
VALUES (1, 1),
       (2000231, 1),
       (3000233, 1),
       (0, 1);