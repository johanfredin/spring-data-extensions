package com.github.johanfredin.springdataextensions.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Useful for domain objects that holds one or several {@link javax.persistence.OneToMany}
 * references in the form of collections.
 */
public interface CollectionHolder {

    <E> Map<String, Collection<E>> getReferences();

    default boolean hasData(String key) {
        return get(key) != null && !get(key).isEmpty();
    }

    default <E> Collection<E> get(String key) {
        return this.<E>getReferences().get(key);
    }

    default <E>Collection<E> put(String key, Collection<E> value) {
        return this.<E>getReferences().put(key, value);
    }

    default <E> E addIfNotExists(String key, E entity) {
        if (!isAlive(key)) {
            put(key, new ArrayList<>());
        }
        if (!get(key).contains(entity)) {
            get(key).add(entity);
        }
        return entity;
    }

    default <E> void deleteEntity(String key, E entity) {
        if (isAlive(key)) {
            get(key).remove(entity);
        }
    }

    default <E> Collection<E> addAll(String key, Collection<E> entities) {
        entities.forEach(e -> addIfNotExists(key, entities));
        return entities;
    }

    default <E> void deleteAll(String key, List<E> entities) {
        if(isAlive(key)) {
            get(key).removeAll(entities);
        }
    }

    default void deleteAll(String key) {
        if(isAlive(key)) {
            get(key).clear();
        }
    }

    default boolean isAlive(String key) {
        return get(key) != null;
    }
}
