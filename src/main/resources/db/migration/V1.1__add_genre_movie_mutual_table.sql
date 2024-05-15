create table if not exists movies_genre_map
(
    movie_id integer
        constraint fk_movies_genres_on_movie_id references movies,
    genre_id integer
        constraint fk_movies_genres_on_genre_id references genres
);