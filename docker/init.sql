CREATE TABLE IF NOT EXISTS users (
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(100) UNIQUE NOT NULL,
    email       VARCHAR(255) UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(50) DEFAULT 'USER',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS broker_accounts (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT REFERENCES users(id),
    account_name    VARCHAR(100) NOT NULL,
    account_id      VARCHAR(100) NOT NULL,
    api_key_hash    VARCHAR(255) NOT NULL,
    api_key_last4   VARCHAR(4),
    broker_type     VARCHAR(50) DEFAULT 'METATRADER',
    active          BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS orders (
    id              VARCHAR(50) PRIMARY KEY,
    user_id         BIGINT REFERENCES users(id),
    broker_account_id BIGINT REFERENCES broker_accounts(id),
    instrument      VARCHAR(20) NOT NULL,
    action          VARCHAR(10) NOT NULL,   -- BUY | SELL
    entry_price     DECIMAL(18,6),
    stop_loss       DECIMAL(18,6),
    take_profit     DECIMAL(18,6),
    status          VARCHAR(20) DEFAULT 'PENDING',   -- PENDING | EXECUTED | CLOSED | FAILED
    raw_signal      TEXT,
    broker_order_id VARCHAR(100),
    closed_price    DECIMAL(18,6),
    pnl             DECIMAL(18,6),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS activity_logs (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT,
    action      VARCHAR(100),
    description TEXT,
    ip_address  VARCHAR(50),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE INDEX IF NOT EXISTS idx_orders_user_id ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_broker_accounts_user_id ON broker_accounts(user_id);
CREATE INDEX IF NOT EXISTS idx_activity_logs_user_id ON activity_logs(user_id);