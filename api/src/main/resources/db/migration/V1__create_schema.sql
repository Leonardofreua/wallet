-- Create custom schema
CREATE SCHEMA IF NOT EXISTS wallet AUTHORIZATION wallet_user;

-- Ensure the search_path includes the wallet schema
ALTER ROLE wallet_user SET search_path TO wallet_service, public;
