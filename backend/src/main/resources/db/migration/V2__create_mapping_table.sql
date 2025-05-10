CREATE TABLE profroles_mapping
(
    id                      BIGSERIAL PRIMARY KEY,
    price_profrole_group_id INT,
    professional_role_id    INT
);

CREATE TABLE region_area_mapping
(
    id              BIGSERIAL PRIMARY KEY,
    price_region_id BIGINT,
    area_id         INT
);


