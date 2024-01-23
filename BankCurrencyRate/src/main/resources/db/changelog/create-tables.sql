--liquibase formatted sql
--changeset group1:create_tables

CREATE TABLE rates_list (
id bigserial PRIMARY KEY,
start_time timestamptz);

CREATE TABLE rates (
id bigserial PRIMARY KEY,
rates_list_id bigint,
buy_rate double precision,
sell_rate double precision,
src_curr VARCHAR(5),
req_cur VARCHAR(5),
	 CONSTRAINT fk_rate_list
	FOREIGN KEY (rates_list_id) REFERENCES rates_list(id)
)

