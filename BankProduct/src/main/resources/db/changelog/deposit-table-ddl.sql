--liquibase formatted sql

--changeset Grigoryev_Pavel:1
CREATE TABLE IF NOT EXISTS deposits
(
    id         BIGSERIAL PRIMARY KEY,
    account_id BIGINT REFERENCES accounts (id) ON DELETE CASCADE NOT NULL,
    term_val   INT                                               NOT NULL,
    term_scale CHAR(1)                                           NOT NULL,
    exp_date   DATE                                              NOT NULL,
    dep_type   VARCHAR(15)                                       NOT NULL,
    auto_renew BOOLEAN                                           NOT NULL
);
