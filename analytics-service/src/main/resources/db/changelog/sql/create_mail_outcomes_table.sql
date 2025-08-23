CREATE TYPE analytics_service."mail_outcome_status" AS ENUM (
    'SUCCESS',
    'FAILED',
    'RETRY'
);

CREATE TABLE IF NOT EXISTS analytics_service."mail_outcomes" (
    id SERIAL PRIMARY KEY,
    campaign_id UUID NOT NULL,
    user_id INTEGER NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    attempt_no SMALLINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    latency_ms SMALLINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);