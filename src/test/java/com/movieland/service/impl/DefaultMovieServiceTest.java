package com.movieland.service.impl;

import com.movieland.common.Currency;
import com.movieland.dto.MovieAdminDto;
import com.movieland.dto.MovieFullInfoDto;
import com.movieland.entity.Country;
import com.movieland.entity.EnrichmentType;
import com.movieland.entity.Genre;
import com.movieland.entity.Movie;
import com.movieland.exception.MovieIncomeDataException;
import com.movieland.mapper.MovieMapper;
import com.movieland.repository.MovieRepository;
import com.movieland.repository.projection.MovieProjection;
import com.movieland.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class DefaultMovieServiceTest {

    @Mock
    private MovieCacheService movieCacheService;
    @Mock
    private EnrichmentService enrichmentService; // required for tests
    @Mock
    private CurrencyConverterService currencyConverterService; // required for tests
    @Mock
    private GenreService genreService;
    @Mock
    private CountryService countryService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private DefaultMovieService defaultMovieService;

    @Test
    @Order(1)
    @DisplayName("Test - find movie by id")
    void givenMovie_whenFindById_thenReturnMovie() {
        // Given
        int movieId = 1;
        Currency currency = Currency.USD;
        MovieFullInfoDto cachedMovie = null;
        Movie movie = createMovie();
        MovieProjection movieProjection = createMockMovieProjection();
        MovieFullInfoDto movieDto = createMovieDto();

        BDDMockito.given(movieCacheService.findInCache(movieId, currency)).willReturn(cachedMovie);
        BDDMockito.given(movieRepository.findByIdProjection(movieId)).willReturn(Optional.of(movieProjection));
        BDDMockito.given(movieMapper.toMovie(movieProjection)).willReturn(movie);
        BDDMockito.given(movieMapper.toMovieFullInfoDto(movie)).willReturn(movieDto);
        BDDMockito.given(movieCacheService.cacheAndReturn(movieId, movieDto)).willReturn(movieDto);

        // When
        MovieFullInfoDto result = defaultMovieService.findMovieById(movieId, currency);

        // Then
        assertNotNull(result);
        assertEquals(movieDto.getId(), result.getId());
        assertEquals(movieDto.getNameRussian(), result.getNameRussian());
        assertEquals(movieDto.getNameNative(), result.getNameNative());
        assertEquals(movieDto.getYearOfRelease(), result.getYearOfRelease());
        assertEquals(movieDto.getDescription(), result.getDescription());
        assertEquals(movieDto.getRating(), result.getRating());
        assertEquals(movieDto.getPrice(), result.getPrice());
        assertEquals(movieDto.getPicturePath(), result.getPicturePath());

        verify(movieCacheService).findInCache(movieId, currency);
        verify(movieRepository).findByIdProjection(movieId);
        verify(movieMapper).toMovie(movieProjection);
        verify(movieMapper).toMovieFullInfoDto(movie);
        verify(movieMapper).postMappingToMovieFullInfoDto(movieDto, movie);
        verify(movieCacheService).cacheAndReturn(movieId, movieDto);
    }

    @Test
    @Order(2)
    @DisplayName("Test - find movie by id, then return from cache")
    void givenMovieInCache_whenFindById_thenReturnMovie() {
        // Given
        int movieId = 1;
        Currency currency = Currency.USD;
        MovieFullInfoDto cachedMovie = createMovieDto();

        BDDMockito.given(movieCacheService.findInCache(movieId, currency)).willReturn(cachedMovie);

        // When
        MovieFullInfoDto result = defaultMovieService.findMovieById(movieId, currency);

        // Then
        assertNotNull(result);
        assertEquals(cachedMovie.getId(), result.getId());
        assertEquals(cachedMovie.getNameRussian(), result.getNameRussian());
        assertEquals(cachedMovie.getNameNative(), result.getNameNative());
        assertEquals(cachedMovie.getYearOfRelease(), result.getYearOfRelease());
        assertEquals(cachedMovie.getDescription(), result.getDescription());
        assertEquals(cachedMovie.getRating(), result.getRating());
        assertEquals(cachedMovie.getPrice(), result.getPrice());
        assertEquals(cachedMovie.getPicturePath(), result.getPicturePath());

        verify(movieCacheService).findInCache(movieId, currency);
    }

    @Test
    @Order(3)
    @DisplayName("Test - find movie by id if id is zero")
    void givenZeroMovieId_whenFindById_thenThrowMovieException() {
        // Given
        int movieId = 0;
        Currency currency = Currency.USD;
        String expectedMessage = "Movie id cannot be zero or negative";

        // Then
        assertThrows(MovieIncomeDataException.class, () -> defaultMovieService.findMovieById(movieId, currency), expectedMessage);
    }

    @Test
    @Order(4)
    @DisplayName("Test - find movie by id if id is negative")
    void givenNegativeMovieId_whenFindById_thenThrowMovieException() {
        // Given
        int movieId = -1;
        Currency currency = Currency.USD;
        String expectedMessage = "Movie id cannot be zero or negative";

        // Then
        assertThrows(MovieIncomeDataException.class, () -> defaultMovieService.findMovieById(movieId, currency), expectedMessage);
    }

    @Test
    @Order(5)
    @DisplayName("Test - update movie")
    void givenMovie_whenUpdate_thenReturnUpdatedMovie() {
        // Given
        int movieId = 1;
        Movie originalMovie = createMovie();

        Movie updatedMovie = createUpdatedMovie();
        MovieAdminDto movieAdminDto = createMovieAdminDto();
        MovieFullInfoDto movieFullInfoDto = createMovieFullInfoDto();

        Genre genreMock = BDDMockito.mock(Genre.class);
        Country countryMock = BDDMockito.mock(Country.class);

        List<Genre> genres = List.of(genreMock);
        List<Country> countries = List.of(countryMock);

        BDDMockito.given(movieRepository.getReferenceById(movieId)).willReturn(originalMovie);
        BDDMockito.given(genreService.findALlById(movieAdminDto.getGenres())).willReturn(genres);
        BDDMockito.given(countryService.findAllCountriesById(movieAdminDto.getCountries())).willReturn(countries);
        BDDMockito.given(movieMapper.update(originalMovie, movieAdminDto, countries, genres)).willReturn(updatedMovie);
        BDDMockito.given(movieMapper.toMovieFullInfoDto(updatedMovie)).willReturn(movieFullInfoDto);
        BDDMockito.given(movieCacheService.cacheAndReturn(movieId, movieFullInfoDto)).willReturn(movieFullInfoDto);

        // When
        MovieFullInfoDto result = defaultMovieService.update(movieId, movieAdminDto);

        // Then
        assertNotNull(result);
        assertEquals(movieFullInfoDto, result);

        verify(movieRepository).getReferenceById(movieId);
        verify(genreService).findALlById(movieAdminDto.getGenres());
        verify(countryService).findAllCountriesById(movieAdminDto.getCountries());
        verify(movieMapper).update(originalMovie, movieAdminDto, countries, genres);
        verify(movieMapper).toMovieFullInfoDto(updatedMovie);
        verify(movieCacheService).cacheAndReturn(movieId, movieFullInfoDto);
    }

    @Test
    @Order(6)
    @DisplayName("Test - update movie, if id zero")
    void givenZeroMovieId_whenUpdate_thenThrowMovieException() {
        // Given
        int movieId = 0;
        MovieAdminDto movieAdminDto = createMovieAdminDto();
        String expectedMessage = "Movie id cannot be zero or negative";

        // Then
        assertThrows(MovieIncomeDataException.class, () -> defaultMovieService.update(movieId, movieAdminDto), expectedMessage);
    }

    @Test
    @Order(7)
    @DisplayName("Test - update movie, if id is negative")
    void givenNegativeMovieId_whenUpdate_thenThrowMovieException() {
        // Given
        int movieId = -1;
        MovieAdminDto movieAdminDto = createMovieAdminDto();
        String expectedMessage = "Movie id cannot be zero or negative";

        // Then
        assertThrows(MovieIncomeDataException.class, () -> defaultMovieService.update(movieId, movieAdminDto), expectedMessage);
    }

    @Test
    @Order(8)
    @DisplayName("Test - update movie, if admin dto is null")
    void givenAdminDtoIsNull_whenUpdate_thenThrowMovieException() {
        // Given
        int movieId = 1;
        MovieAdminDto movieAdminDto = null;
        String expectedMessage = "Income data cannot be null";

        // Then
        assertThrows(MovieIncomeDataException.class, () -> defaultMovieService.update(movieId, movieAdminDto), expectedMessage);
    }

    private MovieFullInfoDto createMovieFullInfoDto() {
        return MovieFullInfoDto.builder()
                .nameRussian("Перси Джексон и Олимпийцы")
                .nameNative("Percy Jackson and the Olympians")
                .yearOfRelease(2023)
                .description("""
                         12-летний Перси Джексон - единственный сын матери-одиночки Салли, проживающий в Нью-Йорке,
                         которому трудно заводить друзей из-за робкого характера, РДВГ и дислексии....
                        """)
                .rating(2.0)
                .price(90.0)
                .picturePath("https://uafilm.pro/18297-percy-jackson-and-the-olympians.html")
                .genres(List.of())
                .countries(List.of())
                .reviews(List.of())
                .build();
    }

    // use lenient() if mock method never used
    private MovieProjection createMockMovieProjection() {
        MovieProjection movieProjection = Mockito.mock(MovieProjection.class);
        Mockito.lenient().when(movieProjection.getId()).thenReturn(1);
        Mockito.lenient().when(movieProjection.getNameRussian()).thenReturn("Перси Джексон и Олимпийцы");
        Mockito.lenient().when(movieProjection.getNameNative()).thenReturn("Percy Jackson and the Olympians");
        Mockito.lenient().when(movieProjection.getYearOfRelease()).thenReturn(2023);
        Mockito.lenient().when(movieProjection.getRating()).thenReturn(2.0);
        Mockito.lenient().when(movieProjection.getPrice()).thenReturn(90.0);
        Mockito.lenient().when(movieProjection.getPicturePath()).thenReturn("https://uafilm.pro/18297-percy-jackson-and-the-olympians.html");
        Mockito.lenient().when(movieProjection.getDescription()).thenReturn("""
                 12-летний Перси Джексон - единственный сын матери-одиночки Салли, проживающий в Нью-Йорке,
                 которому трудно заводить друзей из-за робкого характера, РДВГ и дислексии....
                """);
        return movieProjection;
    }


    private Movie createMovie() {
        return Movie.builder()
                .id(1)
                .nameRussian("Пятьдесят оттенков черного")
                .nameNative("Fifty Shades of Black")
                .yearOfRelease(2016)
                .description("""
                         Феномен успеха эротической мелодрамы «Пятьдесят оттенков серого» многим не дает покоя:
                         одни удивляются, другие возмущаются, а третьи открыто или тайно ждут не дождутся продолжения
                         нашумевшей истории...
                        """)
                .rating(1.0)
                .price(100.0)
                .picturePath("https://uafilm.pro/3820-pyatdesyat-vdtnkv-chornogo.html")
                .build();
    }

    private MovieAdminDto createMovieAdminDto() {
        return MovieAdminDto.builder()
                .nameRussian("Перси Джексон и Олимпийцы")
                .nameNative("Percy Jackson and the Olympians")
                .yearOfRelease(2023)
                .description("""
                         12-летний Перси Джексон - единственный сын матери-одиночки Салли, проживающий в Нью-Йорке,
                         которому трудно заводить друзей из-за робкого характера, РДВГ и дислексии....
                        """)
                .rating(2.0)
                .price(90.0)
                .picturePath("https://uafilm.pro/18297-percy-jackson-and-the-olympians.html")
                .genres(List.of())
                .countries(List.of())
                .build();
    }

    private MovieFullInfoDto createMovieDto() {
        return MovieFullInfoDto.builder()
                .nameRussian("Пятьдесят оттенков черного")
                .nameNative("Fifty Shades of Black")
                .yearOfRelease(2016)
                .description("""
                         Феномен успеха эротической мелодрамы «Пятьдесят оттенков серого» многим не дает покоя:
                         одни удивляются, другие возмущаются, а третьи открыто или тайно ждут не дождутся продолжения
                         нашумевшей истории...
                        """)
                .rating(1.0)
                .price(100.0)
                .picturePath("https://uafilm.pro/3820-pyatdesyat-vdtnkv-chornogo.html")
                .build();
    }

    private Movie createUpdatedMovie() {
        Genre genreMock = BDDMockito.mock(Genre.class);
        Country countryMock = BDDMockito.mock(Country.class);

        return Movie.builder()
                .nameRussian("Перси Джексон и Олимпийцы")
                .nameNative("Percy Jackson and the Olympians")
                .yearOfRelease(2023)
                .description("""
                         12-летний Перси Джексон - единственный сын матери-одиночки Салли, проживающий в Нью-Йорке,
                         которому трудно заводить друзей из-за робкого характера, РДВГ и дислексии....
                        """)
                .rating(2.0)
                .price(90.0)
                .picturePath("https://uafilm.pro/18297-percy-jackson-and-the-olympians.html")
                .countries(List.of(countryMock))
                .genres(List.of(genreMock))
                .build();
    }
}