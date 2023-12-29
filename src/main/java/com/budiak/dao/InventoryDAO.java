package com.budiak.dao;

import com.budiak.model.Inventory;

public class InventoryDAO extends AbstractDAO<Inventory, Integer> {

    public InventoryDAO() {
        super(Inventory.class);
    }
}
