--liquibase formatted sql

--changeset Grigoryev_Pavel:test-1
CREATE TABLE IF NOT EXISTS deposits
(
    acc_iban             VARCHAR(50) PRIMARY KEY,
    customer_id          uuid        NOT NULL,
    customer_type        VARCHAR(15) NOT NULL,
    acc_open_date        DATE        NOT NULL,
    curr_amount          NUMERIC     NOT NULL,
    curr_amount_currency VARCHAR(15) NOT NULL,
    rate                 NUMERIC     NOT NULL,
    term_val             INT         NOT NULL,
    term_scale           CHAR(1)     NOT NULL,
    exp_date             DATE        NOT NULL,
    dep_type             VARCHAR(15) NOT NULL,
    auto_renew           BOOLEAN     NOT NULL
);
