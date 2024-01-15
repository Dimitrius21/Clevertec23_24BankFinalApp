--liquibase formatted sql
--changeset group1:fill_tables contextFilter:dev

INSERT INTO rates_list (start_time) VALUES
('2024-01-10T14:00:00');

INSERT INTO rates (rates_list_id, buy_rate, sell_rate, src_curr, req_cur) VALUES
(1, 3.49, 3.5, 'EUR', 'BYN'),
(1, 3.18, 3.2, 'USD', 'BYN'),
(1, 1.08, 1.11, 'EUR', 'USD');

