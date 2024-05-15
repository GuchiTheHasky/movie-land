alter table if exists movies
    add column description text;



create table if not exists countries
(
    id   integer      not null
        primary key,
    name varchar(255) not null
);

create table if not exists movies_countries_map
(
    movie_id   integer
        constraint fk_movies_countries_on_movie_id references movies,
    country_id integer
        constraint fk_movies_countries_on_country_id references countries
);


create table if not exists users
(
    id       integer      not null
        primary key,
    nickname varchar(255) not null
);


create table if not exists reviews
(
    id       integer not null
        primary key,
    user_id  integer
        constraint fk_reviews_on_user_id
            references users,
    text     text    not null,
    movie_id integer
        constraint fk_reviews_on_movie_id references movies
);
