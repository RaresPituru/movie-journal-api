-- Executed on every start-up (spring.sql.init.mode=always), so every statement must be idempotent.
-- NOTE: "IF NOT EXISTS" never alters an existing table. After changing this file,
-- reset the local database with: docker compose down -v

CREATE TABLE IF NOT EXISTS users (
    id       INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL, -- BCrypt hash, never the raw password
    role     VARCHAR(20)  NOT NULL
);

CREATE TABLE IF NOT EXISTS movie (
    id           INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title        VARCHAR(250)  NOT NULL,
    rating       INT           NOT NULL CHECK (rating BETWEEN 1 AND 10),
    review       VARCHAR(1000),
    release_year INT           NOT NULL,
    user_id      INT           NOT NULL REFERENCES users (id) ON DELETE CASCADE
);

-- PostgreSQL does not index foreign keys automatically, and every movie query filters by user_id.
CREATE INDEX IF NOT EXISTS idx_movie_user_id ON movie (user_id);
