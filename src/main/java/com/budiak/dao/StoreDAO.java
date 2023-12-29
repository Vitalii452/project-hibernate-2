package com.budiak.dao;

import com.budiak.model.Store;

public class StoreDAO extends AbstractDAO<Store, Byte> {

    public StoreDAO() {
        super(Store.class);
    }
}
