-- Index for deposits, withdrawals, and sent transfers
CREATE INDEX idx_tx_log_source_wallet_op_created_at
    ON transaction_log (source_wallet_id, operation, created_at);

-- Index for received transfers
CREATE INDEX idx_tx_log_target_wallet_op_created_at
    ON transaction_log (target_wallet_id, operation, created_at);