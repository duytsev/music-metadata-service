CREATE TABLE artist (
    id UUID PRIMARY KEY,
    name VARCHAR UNIQUE not null,
    idx BIGINT UNIQUE not null
);

CREATE TABLE artist_alias (
    id UUID PRIMARY KEY,
    artist_id UUID REFERENCES artist(id) not null,
    name VARCHAR not null,

    unique (artist_id, name)
);

CREATE TABLE track (
    id UUID PRIMARY KEY,
    artist_id UUID REFERENCES artist(id) not null,
    title VARCHAR not null,
    genre VARCHAR not null,
    length INT not null
);
