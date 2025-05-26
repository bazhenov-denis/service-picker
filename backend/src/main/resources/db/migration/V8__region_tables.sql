DROP TABLE IF EXISTS region_area_mapping;

CREATE TABLE area
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE price_region
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE area_mapping
(
    price_region_id BIGINT NOT NULL,
    area_id         BIGINT NOT NULL,
    PRIMARY KEY (price_region_id, area_id),
    FOREIGN KEY (price_region_id) REFERENCES price_region (id) ON DELETE CASCADE,
    FOREIGN KEY (area_id) REFERENCES area (id) ON DELETE CASCADE
);

INSERT INTO area (id, name)
VALUES (1347, 'Республика Башкортостан'),
       (113, 'Москва');

INSERT INTO price_region (id, name)
VALUES (1347, 'Республика Башкортостан'),
       (1000226, 'Приволжский федеральный округ'),
       (1000001, 'Вся Россия, кроме Москвы и Московской области'),
       (3000233, 'Вся Россия'),
       (0, 'Все регионы'),
       (113, 'Москва'),
       (2000231, 'Москва и московская область');

INSERT INTO area_mapping (price_region_id, area_id)
VALUES (1347, 1347),
       (1000226, 1347),
       (1000001, 1347),
       (3000233, 1347),
       (0, 1347),
       (113, 113),
       (2000231, 113),
       (3000233, 113),
       (0, 113);





