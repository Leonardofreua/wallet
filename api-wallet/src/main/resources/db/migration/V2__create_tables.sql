SET client_encoding TO utf8;

CREATE TABLE IF NOT EXISTS wallet.user (
    id    BIGSERIAL PRIMARY KEY NOT NULL,
    email VARCHAR(255) UNIQUE   NOT NULL
);

ALTER SEQUENCE wallet.user_id_seq OWNER TO wallet_user;
GRANT ALL ON SEQUENCE wallet.user_id_seq TO wallet_user;
GRANT SELECT, UPDATE ON SEQUENCE wallet.user_id_seq TO wallet_user;

CREATE TABLE IF NOT EXISTS wallet.wallet (
    id         BIGSERIAL PRIMARY KEY       NOT NULL,
    user_id    BIGINT                      NOT NULL,
    balance    NUMERIC(19,2)               NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES wallet.user(id)
);

ALTER SEQUENCE wallet.wallet_id_seq OWNER TO wallet_user;
GRANT ALL ON SEQUENCE wallet.wallet_id_seq TO wallet_user;
GRANT SELECT, UPDATE ON SEQUENCE wallet.wallet_id_seq TO wallet_user;

CREATE TABLE IF NOT EXISTS wallet.transaction_log (
    id               BIGSERIAL PRIMARY KEY       NOT NULL,
    correlation_id   UUID                        NOT NULL,
    source_wallet_id BIGINT,
    target_wallet_id BIGINT,
    operation        VARCHAR(20)                 NOT NULL,
    amount           NUMERIC(19,2)               NOT NULL,
    current_status   VARCHAR(20)                 NOT NULL,
    error_message    VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (source_wallet_id) REFERENCES wallet.wallet(id),
    FOREIGN KEY (target_wallet_id) REFERENCES wallet.wallet(id)
);

ALTER SEQUENCE wallet.transaction_log_id_seq OWNER TO wallet_user;
GRANT ALL ON SEQUENCE wallet.transaction_log_id_seq TO wallet_user;
GRANT SELECT, UPDATE ON SEQUENCE wallet.transaction_log_id_seq TO wallet_user;
