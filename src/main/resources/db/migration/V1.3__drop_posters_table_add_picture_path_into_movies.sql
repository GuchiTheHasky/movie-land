alter table if exists movies
    drop constraint fk_movies_on_poster_id;

alter table if exists movies
    drop poster_id;

alter table if exists movies
    add picture_path varchar(255);

drop table if exists posters;
