--CREATE THE TABLES
CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY NOT NULL,
    first_name VARCHAR(255)          NOT NULL,
    last_name  VARCHAR(255)          NOT NULL,
    address_id BIGINT,
    story      TEXT
);

CREATE TABLE IF NOT EXISTS address
(
    id           BIGSERIAL PRIMARY KEY NOT NULL,
    house_number VARCHAR(255)          NOT NULL,
    street_name  VARCHAR(255)          NOT NULL,
    city         VARCHAR(255)          NOT NULL,
    state        VARCHAR(255)          NOT NULL
);

ALTER TABLE users
    ADD COLUMN user_with_weight tsvector;

ALTER TABLE address
    ADD COLUMN address_with_weight tsvector;

--This is to create an index of search_with_weight_idx on users table
CREATE INDEX user_with_weight_idx
    ON users
        USING GIN (user_with_weight);

CREATE INDEX address_with_weight_idx
    ON address
        USING GIN (address_with_weight);

--This is the install pg_trgm extension on the postgres database
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- --This is the function that updates the search_with_weight column for the full text search
CREATE FUNCTION users_tsvector_trigger()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.user_with_weight :=
                    setweight(to_tsvector('simple', coalesce(NEW.first_name, '')), 'A') ||
                    setweight(to_tsvector('simple', coalesce(NEW.last_name, '')), 'B') ||
                    setweight(to_tsvector('english', coalesce(NEW.story, '')), 'C');
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

--This is the trigger that triggers the update function whenever there is a change in users table
CREATE TRIGGER user_tsvectorupdate
    BEFORE INSERT OR UPDATE
    ON users
    FOR EACH ROW
EXECUTE FUNCTION users_tsvector_trigger();

CREATE FUNCTION address_tsvector_trigger()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.address_with_weight :=
                        setweight(to_tsvector('simple', coalesce(NEW.street_name, '')), 'A') ||
                        setweight(to_tsvector('simple', coalesce(NEW.city, '')), 'B') ||
                        setweight(to_tsvector('simple', coalesce(NEW.state, '')), 'C') ||
                        setweight(to_tsvector('simple', coalesce(NEW.house_number, '')), 'D');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER address_tsvectorupdate
    BEFORE INSERT OR UPDATE
    ON address
    FOR EACH ROW
EXECUTE FUNCTION address_tsvector_trigger();