package com.budiak.dao;

import com.budiak.model.Category;

public class CategoryDAO extends AbstractDAO<Category, Byte> {

    public CategoryDAO() {
        super(Category.class);
    }
}
