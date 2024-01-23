--liquibase formatted sql
--changeset group1:fill_tables contextFilter:test

INSERT INTO rates_list (start_time) VALUES
('2024-01-10T14:00:00+03'),
('2024-01-11T14:00:00+03');

INSERT INTO rates (rates_list_id, buy_rate, sell_rate, src_curr, req_cur) VALUES
(1, 3.49, 3.5, 'EUR', 'BYN'),
(1, 3.18, 3.2, 'USD', 'BYN'),
(1, 1.08, 1.11, 'EUR', 'USD'),
(2, 3.48, 3.5, 'EUR', 'BYN'),
(2, 3.16, 3.2, 'USD', 'BYN'),
(2, 1.09, 1.115, 'EUR', 'USD');

