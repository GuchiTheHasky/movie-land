-- Insert movie
INSERT INTO movies (id, name_russian, name_native, year_of_release, description, rating, price, picture_path, version)
VALUES (1, 'Пятьдесят оттенков черного', 'Fifty Shades of Black', 2016,
        'Феномен успеха эротической мелодрамы «Пятьдесят оттенков серого» многим не дает покоя: одни удивляются, другие возмущаются, а третьи открыто или тайно ждут не дождутся продолжения нашумевшей истории...',
        1.0, 100.0, 'https://uafilm.pro/3820-pyatdesyat-vdtnkv-chornogo.html', 0);

-- Insert genres
INSERT INTO genres (id, name) VALUES (1, 'Комедия');
INSERT INTO genres (id, name) VALUES (2, 'Фантастика');
INSERT INTO genres (id, name) VALUES (3, 'Приключения');
INSERT INTO genres (id, name) VALUES (4, 'Семейный');

-- Insert countries
INSERT INTO countries (id, name) VALUES (1, 'США');
INSERT INTO countries (id, name) VALUES (2, 'Италия');
INSERT INTO countries (id, name) VALUES (3, 'Франция');

-- Insert reviews
INSERT INTO reviews (id, text, movie_id) VALUES (1, 'Гениальное кино! Смотришь и думаешь «Так не бывает!», но позже понимаешь, что только так и должно быть...', 1);
INSERT INTO reviews (id, text, movie_id) VALUES (2, 'Кино это является, безусловно, «со знаком качества»...', 1);
INSERT INTO reviews (id, text, movie_id) VALUES (3, 'Перестал удивляться тому, что этот фильм занимает сплошь первые места во всевозможных кино рейтингах...', 1);
INSERT INTO reviews (id, text, movie_id) VALUES (4, 'Много еще можно сказать об этом шедевре...', 1);

-- Insert movies_countries_map
INSERT INTO movies_countries_map (movie_id, country_id) VALUES (1, 1);
INSERT INTO movies_countries_map (movie_id, country_id) VALUES (1, 2);
INSERT INTO movies_countries_map (movie_id, country_id) VALUES (1, 3);

-- Insert movies_genres_map
INSERT INTO movies_genre_map (movie_id, genre_id) VALUES (1, 1);
INSERT INTO movies_genre_map (movie_id, genre_id) VALUES (1, 2);
INSERT INTO movies_genre_map (movie_id, genre_id) VALUES (1, 3);
INSERT INTO movies_genre_map (movie_id, genre_id) VALUES (1, 4);