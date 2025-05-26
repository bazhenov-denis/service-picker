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
    id                BIGSERIAL PRIMARY KEY,
    price_region_id BIGINT NOT NULL,
    area_id       BIGINT NOT NULL,
    PRIMARY KEY (price_region_id, area_id),
    FOREIGN KEY (price_region_id) REFERENCES price_region(id) ON DELETE CASCADE,
    FOREIGN KEY (area_id)         REFERENCES area(id)         ON DELETE CASCADE
);