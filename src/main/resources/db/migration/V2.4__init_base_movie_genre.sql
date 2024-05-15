INSERT INTO movies_genre_map (movie_id, genre_id)
SELECT m.id AS movie_id, g.id AS genre_id
FROM movies m
         CROSS JOIN genres g
WHERE (
    (m.name_russian = 'Побег из Шоушенка' AND g.name IN ('драма', 'криминал'))
        OR (m.name_russian = 'Зеленая миля' AND g.name IN ('фэнтези', 'драма', 'криминал', 'детектив'))
        OR (m.name_russian = 'Форрест Гамп' AND g.name IN ('драма', 'мелодрама'))
        OR (m.name_russian = 'Список Шиндлера' AND g.name IN ('драма', 'биография'))
        OR (m.name_russian = '1+1' AND g.name IN ('драма', 'комедия', 'биография'))
        OR (m.name_russian = 'Начало' AND g.name IN ('фантастика', 'боевик', 'триллер', 'драма', 'детектив'))
        OR (m.name_russian = 'Жизнь прекрасна' AND g.name IN ('драма', 'мелодрама', 'комедия'))
        OR (m.name_russian = 'Бойцовский клуб' AND g.name IN ('триллер', 'драма', 'криминал'))
        OR (m.name_russian = 'Звёздные войны: Эпизод 4 – Новая надежда' AND
            g.name IN ('фантастика', 'фэнтези', 'боевик', 'приключения'))
        OR (m.name_russian = 'Звёздные войны: Эпизод 5 – Империя наносит ответный удар' AND
            g.name IN ('фантастика', 'фэнтези', 'боевик', 'приключения'))
        OR (m.name_russian = 'Унесённые призраками' AND
            g.name IN ('аниме', 'мультфильм', 'фэнтези', 'приключения', 'семейный'))
        OR (m.name_russian = 'Титаник' AND g.name IN ('драма', 'мелодрама'))
        OR (m.name_russian = 'Пролетая над гнездом кукушки' AND g.name = 'драма')
        OR (m.name_russian = 'Ходячий замок' AND
            g.name IN ('аниме', 'мультфильм', 'фэнтези', 'приключения'))
        OR (m.name_russian = 'Гладиатор' AND g.name IN ('боевик', 'драма'))
        OR (m.name_russian = 'Большой куш' AND g.name IN ('криминал', 'комедия'))
        OR (m.name_russian = 'Темный рыцарь' AND
            g.name IN ('фантастика', 'боевик', 'триллер', 'криминал', 'драма'))
        OR (m.name_russian = 'Как приручить дракона' AND
            g.name IN ('мультфильм', 'фэнтези', 'комедия', 'приключения', 'семейный'))
        OR (m.name_russian = 'Молчание ягнят' AND
            g.name IN ('триллер', 'криминал', 'детектив', 'драма'))
        OR (m.name_russian = 'Гран Торино' AND g.name = 'драма')
        OR (m.name_russian = 'Хороший, плохой, злой' AND g.name = 'вестерн')
        OR (m.name_russian = 'Укрощение строптивого' AND g.name = 'комедия')
        OR (m.name_russian = 'Блеф' AND g.name IN ('комедия', 'криминал'))
        OR (m.name_russian = 'Джанго освобожденный' AND
            g.name IN ('драма', 'вестерн', 'приключения', 'комедия'))
        OR (m.name_russian = 'Танцующий с волками' AND g.name IN ('драма', 'приключения', 'вестерн'))
    )
  AND NOT EXISTS (
    SELECT 1
    FROM movies_genre_map mg
    WHERE mg.movie_id = m.id AND mg.genre_id = g.id
);