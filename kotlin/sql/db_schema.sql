CREATE TABLE vacancy
(
    id          UUID PRIMARY KEY,
    title       TEXT NOT NULL,
    salary      BIGINT,
    location    TEXT,
    description TEXT
);

insert into vacancy
values ('01960538-c3fb-74b8-804a-0cada4425619',
        'Java Developer',
        10000,
        'Минск, Беларусь',
        'Работа 5/2 с 9:00 до 18:00 в офисе на Немига 1.');


---------------- schema ----------
CREATE TABLE IF NOT EXISTS label
(
    id   INT,
    name TEXT UNIQUE
);

CREATE TABLE IF NOT EXISTS vacancy
(
    id                UUID PRIMARY KEY,
    title             TEXT   NOT NULL,
    salary_min        BIGINT,
    salary_max        BIGINT,
    salary_currency   TEXT,
    work_type         TEXT, -- remote, office, hybrid
    location          TEXT,
    description       TEXT,
    experience_level  TEXT, -- NO, 1_year, 3_years, 6+years
    views_count       INT,
    employer_id       UUID   NOT NULL,
    created_at_millis BIGINT NOT NULL,
    edited_at_millis  BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS vacancy_label
(
    vacancy_id UUID,
    label_id   INT,
    PRIMARY KEY (vacancy_id, label_id)
);

CREATE TABLE IF NOT EXISTS "user"
(
    id                   UUID PRIMARY KEY,
    email                TEXT UNIQUE NOT NULL,
    password_hash        TEXT        NOT NULL,
    email_verified       BOOLEAN     NOT NULL,
    role                 TEXT        NOT NULL,
    registered_at_millis BIGINT      NOT NULL,
    status               TEXT, -- blocked, active
    first_name           TEXT,
    last_name            TEXT,
    middle_name          TEXT,
    birth_date           DATE
);

CREATE TABLE IF NOT EXISTS company_details
(
    user_id          UUID PRIMARY KEY,
    office_location  TEXT,
    employees_amount INT,
    description      TEXT,
    founded_at       DATE
);

CREATE TABLE IF NOT EXISTS resume
(
    id                UUID PRIMARY KEY,
    user_id           UUID NOT NULL,
    title             TEXT NOT NULL,
    summary           TEXT,
    created_at_millis BIGINT,
    edited_at_millis  BIGINT,
    is_active         BOOLEAN DEFAULT TRUE,
    experience        JSONB
);

CREATE TABLE IF NOT EXISTS resume_label
(
    resume_id UUID,
    label_id  INT,
    PRIMARY KEY (resume_id, label_id)
);

