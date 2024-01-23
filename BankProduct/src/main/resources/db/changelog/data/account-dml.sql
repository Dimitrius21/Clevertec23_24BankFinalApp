--liquibase formatted sql
--changeset group1:fill_tables contextFilter:dev

INSERT INTO accounts (iban, name, amount, currency_code, open_date, main_acc, customer_id, customer_type, rate) VALUES
('000000000000000000000000000', 'Test', 0, 'BYN', '2024-01-01', true, '00000000-0000-0000-0000-000000000000', 'PHYSIC', 0.0)
