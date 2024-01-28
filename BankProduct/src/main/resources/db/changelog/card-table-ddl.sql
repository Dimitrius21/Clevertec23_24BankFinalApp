--liquibase formatted sql

--changeset Savitsky_E:2

CREATE TABLE IF NOT EXISTS cards
(
       card_number     VARCHAR(16) PRIMARY KEY,
       iban            VARCHAR(27) NOT NULL CONSTRAINT cards_accounts_iban_fk REFERENCES accounts,
       customer_id     uuid        NOT NULL,
       customer_type   VARCHAR(10) NOT NULL,
       cardholder      VARCHAR(50) NOT NULL,
       card_status     VARCHAR(10) NOT NULL
);