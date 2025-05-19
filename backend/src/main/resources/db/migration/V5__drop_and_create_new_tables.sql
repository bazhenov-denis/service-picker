DROP TABLE IF EXISTS profroles_mapping;

CREATE TABLE price_profrole (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE profrole (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE profrole_group_mapping (
    id BIGSERIAL PRIMARY KEY,
    price_profrole_id BIGINT NOT NULL,
    profrole_id BIGINT NOT NULL,
    CONSTRAINT fk_group_mapping_price_profrole
        FOREIGN KEY (price_profrole_id)
        REFERENCES price_profrole (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_group_mapping_profrole
        FOREIGN KEY (profrole_id)
        REFERENCES profrole (id)
        ON DELETE CASCADE
);