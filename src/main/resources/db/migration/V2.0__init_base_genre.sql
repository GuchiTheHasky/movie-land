alter table genres alter column id set default nextval('genres_id_sequence');

insert into genres (name)
values ('драма'),
       ('криминал'),
       ('фэнтези'),
       ('детектив'),
       ('мелодрама'),
       ('биография'),
       ('комедия'),
       ('фантастика'),
       ('боевик'),
       ('триллер'),
       ('приключения'),
       ('аниме'),
       ('мультфильм'),
       ('семейный'),
       ('вестерн');
