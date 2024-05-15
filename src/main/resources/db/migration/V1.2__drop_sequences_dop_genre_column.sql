drop sequence if exists genres_id_sequence cascade;

drop sequence if exists movies_id_sequence cascade;

drop sequence if exists posters_id_sequence cascade;

alter table if exists movies
    drop genre;