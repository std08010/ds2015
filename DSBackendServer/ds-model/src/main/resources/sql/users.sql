DROP INDEX IF EXISTS users_username_idx;

CREATE INDEX users_username_idx ON users (username ASC);