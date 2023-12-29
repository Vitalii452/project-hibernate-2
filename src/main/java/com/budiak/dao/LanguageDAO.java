package com.budiak.dao;

import com.budiak.model.Language;

public class LanguageDAO extends AbstractDAO<Language, Byte> {

    public LanguageDAO() {
        super(Language.class);
    }
}
