DROP TABLE IF EXISTS offers;

CREATE TABLE offers (
                        id BIGSERIAL PRIMARY KEY,
                        product_id        INT,
                        tariff            VARCHAR(50),
                        code              VARCHAR(50),
                        child_code_1      VARCHAR(50),
                        child_count_1     INT,
                        child_code_2      VARCHAR(50),
                        child_count_2     INT,
                        child_code_3      VARCHAR(50),
                        child_count_3     INT,
                        child_code_4      VARCHAR(50),
                        child_count_4     INT,
                        period            INT,
                        region_id         INT,
                        profrole_group_id INT,
                        price_all         NUMERIC,
                        currency          VARCHAR(10)
);


DELETE FROM flyway_schema_history WHERE version = '1';
