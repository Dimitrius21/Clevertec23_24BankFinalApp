--liquibase formatted sql

--changeset Grigoryev_Pavel:test-1
CREATE TABLE IF NOT EXISTS customer
(
    customer_id        UUID PRIMARY KEY,
    customer_type      VARCHAR(20)        NOT NULL,
    unp                VARCHAR(9),
    register_date      DATE               NOT NULL,
    email              VARCHAR(50) UNIQUE NOT NULL,
    phone_code         VARCHAR(10)        NOT NULL,
    phone_number       VARCHAR(20)        NOT NULL,
    customer_full_name VARCHAR(255)       NOT NULL,
    deleted            BOOLEAN            NOT NULL
);
