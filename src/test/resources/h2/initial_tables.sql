CREATE TABLE IF NOT EXISTS movies
(
    id              BIGINT PRIMARY KEY,
    name_russian    VARCHAR(255),
    name_native     VARCHAR(255),
    year_of_release INT,
    description     TEXT,
    rating          DOUBLE,
    price           DOUBLE,
    picture_path    VARCHAR(255),
    version         INT
);

CREATE TABLE IF NOT EXISTS genres
(
    id       BIGINT PRIMARY KEY,
    name     VARCHAR(255),
    movie_id BIGINT,
    FOREIGN KEY (movie_id) REFERENCES movies (id)
);

CREATE TABLE IF NOT EXISTS countries
(
    id       BIGINT PRIMARY KEY,
    name     VARCHAR(255),
    movie_id BIGINT,
    FOREIGN KEY (movie_id) REFERENCES movies (id)
);

CREATE TABLE IF NOT EXISTS reviews
(
    id       BIGINT PRIMARY KEY,
    user_id  integer,
    text     TEXT,
    movie_id BIGINT,
    FOREIGN KEY (movie_id) REFERENCES movies (id)
);

CREATE TABLE IF NOT EXISTS movies_countries_map
(
    movie_id   integer
        constraint fk_movies_countries_on_movie_id
            references movies,
    country_id integer
        constraint fk_movies_countries_on_country_id
            references countries
);

create table IF NOT EXISTS movies_genre_map
(
    movie_id integer
        constraint fk_movies_genres_on_movie_id
            references movies,
    genre_id integer
        constraint fk_movies_genres_on_genre_id
            references genres
);

