--liquibase formatted sql
--changeset group1:fill_tables contextFilter:test

INSERT INTO accounts (iban, name, amount, currency_code, open_date, main_acc, customer_id, customer_type, rate) VALUES
('000000000000000000000000000', 'Test', 0, 'BYN', '2024-01-01', true, '00000000-0000-0000-0000-000000000000', 'PHYSIC', 0.0),
('AABBCCCDDDDEEEEEEEEEEEEEEE0', 'Main', 20000, 'BYN', '2024-01-07', true, '1a72a05f-4b8f-43c5-a889-1ebc6d9dc729', 'LEGAL', 0.0),
('AABBCCCDDDDEEEEEEEEEEEEEEE1', 'Main', 10000, 'USD', '2024-01-08', true, '1a72a05f-4b8f-43c5-a889-1ebc6d9dc729', 'LEGAL', 0.0),
('AABBCCCDDDDEEEEEEEEEEEEEEEF', 'Main', 30000, 'BYN', '2024-01-09', true, '1a72a05f-4b8f-43c5-a889-1ebc6d9dc730', 'LEGAL', 0.0),
('AABBCCCDDDDEEEEEEEE01010101', 'Main', 30000, 'BYN', '2024-01-10', true, '1a72a05f-4b8f-43c5-a889-1ebc6d9dc731', 'PHYSIC', 0.01),
('AABBCCCDDDDEEEEEEEE01010102', 'Main', 40000, 'BYN', '2024-01-11', true, '1a72a05f-4b8f-43c5-a889-1ebc6d9dc732', 'PHYSIC', 0.01);

INSERT INTO cards (cardNumber, iban, customer_id, customer_type, cardholder, cardStatus VALUES
('5200001000001096', 'AABBCCCDDDDEEEEEEEEEEEEEEEF', '1a72a05f-4b8f-43c5-a889-1ebc6d9dc730', 'PHYSIC', 'Ivanov Ivan', 'ACTIVE')