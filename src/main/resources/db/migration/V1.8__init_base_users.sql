alter table users
    alter column id set default nextval('users_id_sequence');


insert into users (nickname, email, password)
values ('Рональд Рейнольдс', 'ronald.reynolds66@example.com', 'paco'),
       ('Дарлин Эдвардс', 'darlene.edwards15@example.com', 'bricks'),
       ('Габриэль Джексон', 'gabriel.jackson91@example.com', 'hjkl'),
       ('Дэрил Брайант', 'daryl.bryant94@example.com', 'exodus'),
       ('Нил Паркер', 'neil.parker43@example.com', '878787'),
       ('Трэвис Райт', 'travis.wright36@example.com', 'smart'),
       ('Амелия Кэннеди', 'amelia.kennedy58@example.com', 'beaker'),
       ('Айда Дэвис', 'ida.davis80@example.com', 'pepsi1'),
       ('Джесси Паттерсон', 'jessie.patterson68@example.com', 'tommy'),
       ('Деннис Крейг', 'dennis.craig82@example.com', 'coldbeer');
