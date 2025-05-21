ALTER TABLE profrole_group_mapping DROP CONSTRAINT profrole_group_mapping_pkey;
ALTER TABLE profrole_group_mapping DROP COLUMN id;

ALTER TABLE profrole_group_mapping
    ADD PRIMARY KEY (price_profrole_id, profrole_id);