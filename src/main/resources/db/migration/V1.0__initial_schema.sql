create sequence genres_id_sequence
    increment by 1;


create table if not exists genres
(
    id   integer default nextval('genres_id_sequence'::regclass) not null
        primary key,
    name varchar(255)                                            not null
);

alter sequence genres_id_sequence owned by genres.id;


create sequence posters_id_sequence
    increment by 1;


create table if not exists posters
(
    id           integer default nextval('posters_id_sequence'::regclass) not null
        primary key,
    movie_name   varchar(255)                                             not null,
    picture_path varchar(255)                                             not null
);

alter sequence posters_id_sequence owned by posters.id;


create sequence movies_id_sequence
    increment by 1;



create table if not exists movies
(
    id              integer default nextval('movies_id_sequence'::regclass) not null
        primary key,
    name_russian    varchar(255)                                            not null,
    name_native     varchar(255)                                            not null,
    year_of_release integer                                                 not null,
    genre           varchar(255)                                            not null,
    rating          numeric(8, 2)                                           not null,
    price           numeric(8, 2)                                           not null,
    poster_id       integer                                                 not null
        constraint fk_movies_on_poster_id
            references posters
);

alter sequence movies_id_sequence owned by movies.id;