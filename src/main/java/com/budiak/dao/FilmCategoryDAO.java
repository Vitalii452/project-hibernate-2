package com.budiak.dao;

import com.budiak.model.FilmCategory;

public class FilmCategoryDAO extends AbstractDAO<FilmCategory, FilmCategory.Id> {

    public FilmCategoryDAO() {
        super(FilmCategory.class);
    }
}
