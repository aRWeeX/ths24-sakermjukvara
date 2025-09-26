-- Create a restricted user in H2
-- Encrypted password: $2a$10$m5xIh1SB3o62MPbQErgUO.yQ0Gg9yQPzpnnyec53V/dfjlZslnbQO
CREATE USER IF NOT EXISTS app_user PASSWORD 'F^Dpsh9Q!MbFGhY!gWYh';

-- Grant only basic privileges (H2 is limited compared to Postgres/MySQL,
-- but this simulates SELECT/INSERT/UPDATE/DELETE)
GRANT SELECT, INSERT, UPDATE, DELETE ON SCHEMA PUBLIC TO app_user;
