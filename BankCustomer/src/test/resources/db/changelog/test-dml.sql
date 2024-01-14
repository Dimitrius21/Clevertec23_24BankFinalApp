--liquibase formatted sql

--changeset Grigoryev_Pavel:test-2
INSERT INTO customer (customer_id, customer_type, unp, register_date, email, phone_code, phone_number,
                      customer_full_name, deleted)
VALUES ('1a72a05f-4b8f-43c5-a889-1ebc6d9dc729', 'LEGAL', '123AB6789', '2024.01.01', 'HornsAndHooves@example.com',
        '37529', '1111111', 'ООО Рога и копыта', false),
       ('1a72a05f-4b8f-43c5-a889-1ebc6d9dc929', 'LEGAL', '98S65D321', '2024.01.02', 'JollyRoger@example.com', '37533',
        '2222222', 'ООО Веселый роджер', false),
       ('1a72a05f-4b8f-43c5-a889-1ebc6d9dc727', 'PHYSIC', NULL, '2024.01.03', 'Ivanov@example.com', '37544', '3333333',
        'Иванов Иван Иванович', false),
       ('1a72a05f-4b8f-43c5-a889-1ebc6d9dc721', 'PHYSIC', NULL, '2024.01.04', 'Petrov@example.com', '37525', '4444444',
        'Петров Петр Петрович', false),
       ('1a72a05f-4b8f-43c5-a889-1ebc6d9dc722', 'PHYSIC', NULL, '2024.01.05', 'Sidorovich@example.com', '37517',
        '5555555', 'Сидоров Сидор Сидорович', false),
       ('1a72a05f-4b8f-43c5-a889-1ebc6d9dc723', 'LEGAL', '4567F91I3', '2024.01.06', 'StarTrek@example.com', '37529',
        '6666666', 'ООО Звездный путь', false),
       ('1a72a05f-4b8f-43c5-a889-1ebc6d9dc724', 'LEGAL', '3GH789R56', '2024.01.07', 'Matrix@example.com', '37533',
        '7777777', 'ООО Матрица', false),
       ('1a72a05f-4b8f-43c5-a889-1ebc6d9dc725', 'PHYSIC', NULL, '2024.01.08', 'Novik@example.com', '37544', '8888888',
        'Новиков Василий Борисович', false),
       ('1a72a05f-4b8f-43c5-a889-1ebc6d9dc726', 'PHYSIC', NULL, '2024.01.09', 'Starik@example.com', '37525', '9999999',
        'Стариков Сергей Васильевич', false),
       ('1a72a05f-4b8f-43c5-a889-1ebc6d9dc529', 'PHYSIC', NULL, '2024.01.10', 'Alekseev@example.com', '37517',
        '0000000', 'Алексеев Алексей Алексеевич', false);
