--liquibase formatted sql
--changeset group1:fill_tables contextFilter:test

INSERT INTO accounts (iban, name, amount, currency_code, open_date, main_acc, customer_id, customer_type, rate) VALUES
('AABBCCCDDDDEEEEEEEEEEEEEEE0', 'Main', 20000, 'BYN', '2024-01-07', true, '1a72a05f-4b8f-43c5-a889-1ebc6d9dc729', 'LEGAL', 0.0),
('AABBCCCDDDDEEEEEEEEEEEEEEE1', 'Main', 10000, 'USD', '2024-01-08', true, '1a72a05f-4b8f-43c5-a889-1ebc6d9dc729', 'LEGAL', 0.0),
('AABBCCCDDDDEEEEEEEEEEEEEEEF', 'Main', 30000, 'BYN', '2024-01-09', true, '1a72a05f-4b8f-43c5-a889-1ebc6d9dc730', 'LEGAL', 0.0),
('AABBCCCDDDDEEEEEEEE01010101', 'Main', 30000, 'BYN', '2024-01-10', true, '1a72a05f-4b8f-43c5-a889-1ebc6d9dc727', 'PHYSIC', 0.01),
('AABBCCCDDDDEEEEEEEE01010102', 'Main', 40000, 'BYN', '2024-01-11', true, '1a72a05f-4b8f-43c5-a889-1ebc6d9dc726', 'PHYSIC', 0.01);

INSERT INTO cards (card_number, iban, customer_id, customer_type, cardholder, card_status) VALUES
('5200001000001096', 'AABBCCCDDDDEEEEEEEE01010101', '1a72a05f-4b8f-43c5-a889-1ebc6d9dc727', 'PHYSIC', 'Ivanov Ivan', 'ACTIVE');