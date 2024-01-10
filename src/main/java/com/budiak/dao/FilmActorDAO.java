package com.budiak.dao;

import com.budiak.model.FilmActor;

public class FilmActorDAO extends AbstractDAO<FilmActor, FilmActor.Id> {

    public FilmActorDAO() {
        super(FilmActor.class);
    }
}
