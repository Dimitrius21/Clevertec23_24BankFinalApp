--liquibase formatted sql

--changeset Grigoryev_Pavel:2
INSERT INTO deposits (acc_iban, customer_id, customer_type, acc_open_date, curr_amount, curr_amount_currency,
                      rate,
                      term_val, term_scale, exp_date, dep_type, auto_renew)
VALUES ('FR7630001007941234567890185', '1a72a05f-4b8f-43c5-a889-1ebc6d9dc729', 'LEGAL',
        '2024-01-01', 10000.00, 'EUR', 0.05, 12, 'D', '2024-01-13', 'REVOCABLE', TRUE),
       ('DE89370400440532013000', '1a72a05f-4b8f-43c5-a889-1ebc6d9dc929', 'PHYSIC',
        '2024-01-02', 50000.50, 'EUR', 0.03, 6, 'M', '2024-06-30', 'IRREVOCABLE', FALSE),
       ('GB29NWBK60161331926819', '1a72a05f-4b8f-43c5-a889-1ebc6d9dc727', 'LEGAL',
        '2024-01-03', 2000.33, 'GBP', 0.02, 3, 'M', '2024-03-31', 'REVOCABLE', TRUE),
       ('SA0380000000608010167519', '1a72a05f-4b8f-43c5-a889-1ebc6d9dc721', 'LEGAL',
        '2024-01-04', 100000.44, 'SAR', 0.04, 1, 'D', '2024-01-02', 'IRREVOCABLE', FALSE),
       ('CH9300762011623852957', '1a72a05f-4b8f-43c5-a889-1ebc6d9dc722', 'PHYSIC',
        '2024-01-05', 5000.10, 'CHF', 0.01, 1, 'M', '2024-02-28', 'REVOCABLE', TRUE),
       ('TR330006100519786457841326', '1a72a05f-4b8f-43c5-a889-1ebc6d9dc723', 'LEGAL',
        '2024-01-06', 30000.66, 'TRY', 0.06, 9, 'M', '2024-09-30', 'REVOCABLE', FALSE),
       ('AE070331234567890123456', '1a72a05f-4b8f-43c5-a889-1ebc6d9dc724', 'PHYSIC',
        '2024-01-07', 15000.00, 'AED', 0.03, 6, 'M', '2024-06-30', 'IRREVOCABLE', TRUE),
       ('IL620108000000099999999', '1a72a05f-4b8f-43c5-a889-1ebc6d9dc725', 'LEGAL',
        '2024-01-08', 40000.75, 'ILS', 0.02, 3, 'M', '2024-03-31', 'REVOCABLE', FALSE),
       ('AU8832329000000000000', '1a72a05f-4b8f-43c5-a889-1ebc6d9dc726', 'PHYSIC',
        '2024-01-09', 1000.80, 'AUD', 0.01, 1, 'M', '2024-01-31', 'IRREVOCABLE', TRUE),
       ('CA2600000000000000000', '1a72a05f-4b8f-43c5-a889-1ebc6d9dc529', 'LEGAL', '2024-01-10',
        20000.99, 'CAD', 0.02, 3, 'M', '2024-03-31', 'IRREVOCABLE', FALSE);
