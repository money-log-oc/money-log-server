CREATE TABLE IF NOT EXISTS budget_settings (
    id BIGSERIAL PRIMARY KEY,
    payday_day INTEGER NOT NULL,
    monthly_budget BIGINT NOT NULL,
    week_start VARCHAR(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    occurred_at TIMESTAMP NOT NULL,
    merchant VARCHAR(255) NOT NULL,
    amount BIGINT NOT NULL,
    excluded BOOLEAN NOT NULL DEFAULT FALSE,
    exclusion_reason VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS transaction_tags (
    transaction_id BIGINT NOT NULL,
    tag VARCHAR(255) NOT NULL,
    CONSTRAINT fk_transaction_tags_transaction
        FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE,
    CONSTRAINT uq_transaction_tags UNIQUE (transaction_id, tag)
);

CREATE INDEX IF NOT EXISTS idx_transactions_occurred_at ON transactions(occurred_at);
CREATE INDEX IF NOT EXISTS idx_transactions_excluded ON transactions(excluded);
