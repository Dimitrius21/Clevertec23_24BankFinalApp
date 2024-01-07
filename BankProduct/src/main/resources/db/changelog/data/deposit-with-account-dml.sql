--liquibase formatted sql

--changeset Grigoryev_Pavel:2
INSERT INTO accounts (name, iban, iban_readable, amount, currency_code, open_date, main_acc, customer_id, customer_type,
                      rate)
VALUES ('Сбережения', 'BY86AKBB10100000002966000000', 'BY86 AKBB 1010 0000 0029 6600 0000', 500000, 933, '2024.01.01',
        true, '1a72a05f-4b8f-43c5-a889-1ebc6d9dc729', 'LEGAL', 0.02),
       ('Расчетный', 'BY86AKBB10100000002967000000', 'BY86 AKBB 1010 0000 0029 6700 0000', 100000, 933, '2024.01.01',
        false, '1a72a05f-4b8f-43c5-a889-1ebc6d9dc729', 'LEGAL', 0.01),
       ('Валютный', 'BY86AKBB10100000002968000000', 'BY86 AKBB 1010 0000 0029 6800 0000', 10000, 840, '2024.01.01',
        false, '1a72a05f-4b8f-43c5-a889-1ebc6d9dc729', 'LEGAL', 0.03),
       ('Зарплатный', 'BY86AKBB10100000002969000000', 'BY86 AKBB 1010 0000 0029 6900 0000', 200000, 933, '2024.01.01',
        false, '2b83a06f-4b9f-43c6-a889-2ebc6d9dc730', 'PHYSIC', 0.01),
       ('Депозитный', 'BY86AKBB10100000002970000000', 'BY86 AKBB 1010 0000 0029 7000 0000', 300000, 933, '2024.01.01',
        false, '2b83a06f-4b9f-43c6-a889-2ebc6d9dc730', 'PHYSIC', 0.05);

INSERT INTO deposits (account_id, term_val, term_scale, exp_date, dep_type, auto_renew)
VALUES (1, 12, 'M', '2025.01.01', 'IRREVOCABLE', false),
       (1, 6, 'M', '2024.07.01', 'REVOCABLE', true),
       (1, 3, 'D', '2024.01.04', 'REVOCABLE', false),
       (2, 24, 'M', '2026.01.01', 'IRREVOCABLE', false),
       (2, 12, 'M', '2025.01.01', 'REVOCABLE', true),
       (2, 1, 'D', '2024.01.02', 'REVOCABLE', false),
       (3, 12, 'M', '2025.01.01', 'IRREVOCABLE', false),
       (3, 6, 'M', '2024.07.01', 'REVOCABLE', true),
       (3, 4, 'D', '2024.01.05', 'REVOCABLE', false),
       (4, 12, 'M', '2025.01.01', 'IRREVOCABLE', false),
       (4, 6, 'M', '2024.07.01', 'REVOCABLE', true),
       (4, 15, 'D', '2024.01.16', 'REVOCABLE', false),
       (5, 12, 'M', '2025.01.01', 'IRREVOCABLE', false),
       (5, 6, 'M', '2024.07.01', 'REVOCABLE', true),
       (5, 21, 'D', '2024.01.22', 'REVOCABLE', false);
