CREATE TABLE IF NOT EXISTS credits
(
    contract_number     VARCHAR(20)      PRIMARY KEY,
    customer_id         UUID             NOT NULL,
    contract_start_date DATE             NOT NULL,
    total_debt          BIGINT           NOT NULL,
    current_debt        BIGINT           NOT NULL,
    currency            VARCHAR(7)       NOT NULL,
    repayment_date      DATE             NOT NULL,
    rate                DOUBLE PRECISION NOT NULL,
    iban                VARCHAR(35)      NOT NULL,
    possible_repayment  BOOLEAN          NOT NULL,
    is_closed           BOOLEAN          NOT NULL,
    customer_type       VARCHAR(25)      NOT NULL
);