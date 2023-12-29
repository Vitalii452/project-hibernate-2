package com.budiak.dao;

import com.budiak.model.Actor;

public class ActorDAO extends AbstractDAO<Actor, Short> {

    public ActorDAO() {
        super(Actor.class);
    }
}
