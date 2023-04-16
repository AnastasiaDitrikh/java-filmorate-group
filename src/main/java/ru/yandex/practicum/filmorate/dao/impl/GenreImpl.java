package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    @Override
    public Genre get(int genreId) {
        if (!isExist(genreId)) {
            throw new ObjectNotFoundException("Жанр не найден");
        }
        String sql = "SELECT* FROM genres WHERE genre_id=?";
        return jdbcTemplate.queryForObject(sql, genreMapper::mapRow, genreId);
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT* FROM genres";
        return jdbcTemplate.query(sql, genreMapper);
    }

    @Override
    public void delete(long filmId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public List<Genre> getGenresListForFilm(long filmId) {
        String sql = "SELECT fg.*, g.genre_name FROM film_genre AS fg JOIN genres AS g ON g.genre_id = fg.genre_id " +
                        "WHERE fg.film_id = ?";
        return jdbcTemplate.query(sql, genreMapper::mapRow, filmId);
    }

    @Override
    public List<Genre> add(long filmId, List<Genre> genres) {
        String sql = "MERGE INTO film_genre (film_id, genre_id) KEY(film_id, genre_id) VALUES (?, ?)";
        if (genres == null || genres.isEmpty()) {
            return new ArrayList<>();
        }
        for (Genre genre : genres) {
            jdbcTemplate.update(sql, filmId, genre.getId());
        }
        return getGenresListForFilm(filmId);
    }

    private boolean isExist(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE genre_id = ?", id);
        return userRows.next();
    }

}