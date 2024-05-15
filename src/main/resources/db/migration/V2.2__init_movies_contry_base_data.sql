INSERT INTO movies_countries_map (movie_id, country_id)
SELECT m.id AS movie_id, c.id AS country_id
FROM movies m
         CROSS JOIN countries c
WHERE (
    (m.name_russian = 'Побег из Шоушенка' AND c.name = 'США')
        OR (m.name_russian = 'Зеленая миля' AND c.name = 'США')
        OR (m.name_russian = 'Форрест Гамп' AND c.name = 'США')
        OR (m.name_russian = 'Список Шиндлера' AND c.name = 'США')
        OR (m.name_russian = '1+1' AND c.name = 'Франция')
        OR (m.name_russian = 'Начало' AND c.name IN ('США', 'Великобритания'))
        OR (m.name_russian = 'Жизнь прекрасная' AND c.name = 'Италия')
        OR (m.name_russian = 'Бойцовский клуб' AND c.name IN ('США', 'Германия'))
        OR (m.name_russian = 'Звёздные войны: Эпизод 4 – Новая надежда' AND c.name = 'США')
        OR (m.name_russian = 'Звёздные войны: Эпизод 5 – Империя наносит ответный удар' AND c.name = 'США')
        OR (m.name_russian = 'Унесённые призраками' AND c.name = 'Япония')
        OR (m.name_russian = 'Титаник' AND c.name = 'США')
        OR (m.name_russian = 'Пролетая над гнездом кукушки' AND c.name = 'США')
        OR (m.name_russian = 'Ходячий замок' AND c.name = 'Япония')
        OR (m.name_russian = 'Гладиатор' AND c.name IN ('США', 'Великобритания'))
        OR (m.name_russian = 'Большой куш' AND c.name IN ('Великобритания', 'США'))
        OR (m.name_russian = 'Темный рыцарь' AND c.name IN ('США', 'Великобритания'))
        OR (m.name_russian = 'Как приручить дракона' AND c.name = 'США')
        OR (m.name_russian = 'Молчание ягнят' AND c.name = 'США')
        OR (m.name_russian = 'Гран Торино' AND c.name IN ('США', 'Германия'))
        OR (m.name_russian = 'Хороший, плохой, злой' AND c.name IN ('Италия', 'Испания', 'Германия', 'США'))
        OR (m.name_russian = 'Укрощение строптивого' AND c.name = 'Италия')
        OR (m.name_russian = 'Блеф' AND c.name = 'Италия')
        OR (m.name_russian = 'Джанго освобожденный' AND c.name = 'США')
        OR (m.name_russian = 'Танцующий с волками' AND c.name IN ('США', 'Великобритания'))
    )
  AND NOT EXISTS (SELECT 1
                  FROM movies_countries_map mc
                  WHERE mc.movie_id = m.id
                    AND mc.country_id = c.id);