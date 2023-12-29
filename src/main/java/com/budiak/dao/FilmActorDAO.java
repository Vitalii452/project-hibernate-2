package com.budiak.dao;

import com.budiak.model.FilmActor;

public class FilmActorDAO extends AbstractDAO<FilmActor, Long> {

    public FilmActorDAO() {
        super(FilmActor.class);
    }
}
