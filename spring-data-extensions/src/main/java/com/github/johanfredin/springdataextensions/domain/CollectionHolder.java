package com.github.johanfredin.springdataextensions.domain;

import java.util.ArrayList;
import java.util.List;

public interface CollectionHolder<E extends Identifiable> {

    List<E> getCollection();

    void setCollection(List<E> collection);

    default boolean hasData() {
        return getCollection() != null && !getCollection().isEmpty();
    }

    default E addIfNotExists(E entity) {
        if (!isAlive()) {
            setCollection(new ArrayList<>());
        }
        if (!getCollection().contains(entity)) {
            getCollection().add(entity);
        }
        return entity;
    }

    default void deleteEntity(E entity) {
        if (isAlive()) {
            getCollection().remove(entity);
        }
    }

    default List<E> addAll(List<E> entities) {
        entities.forEach(this::addIfNotExists);
        return entities;
    }

    default void deleteAll(List<E> entities) {
        if(isAlive()) {
            getCollection().removeAll(entities);
        }
    }

    default void deleteAll() {
        if(isAlive()) {
            getCollection().clear();
        }
    }

    default boolean isAlive() {
        return getCollection() != null;
    }
}
