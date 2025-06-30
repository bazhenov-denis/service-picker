ALTER TABLE offers
    ADD CONSTRAINT uq_offers_product_id UNIQUE (product_id);
