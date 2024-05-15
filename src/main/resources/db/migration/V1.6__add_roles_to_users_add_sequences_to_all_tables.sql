alter table if exists users
    add column role varchar(255);

create sequence reviews_id_sequence
    increment by 1;

alter sequence reviews_id_sequence owned by reviews.id;


create sequence movies_id_sequence
    increment by 1;

alter sequence movies_id_sequence owned by movies.id;


create sequence users_id_sequence
    increment by 1;

alter sequence users_id_sequence owned by users.id;


create sequence genres_id_sequence
    increment by 1;

alter sequence genres_id_sequence owned by genres.id;


create sequence countries_id_sequence
    increment by 1;

alter sequence countries_id_sequence owned by countries.id;



