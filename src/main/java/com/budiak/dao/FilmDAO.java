package com.budiak.dao;

import com.budiak.model.Film;

public class FilmDAO extends AbstractDAO<Film, Short> {

    public FilmDAO() {
        super(Film.class);
    }
}
