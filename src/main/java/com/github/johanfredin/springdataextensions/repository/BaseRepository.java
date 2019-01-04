/*
 * Copyright 2018 Johan Fredin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.domain.Identifiable;
import com.github.johanfredin.springdataextensions.util.CollectionHelper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.*;

/**
 * Abstract superclass for all repositories used, extends springs {@link CrudRepository}
 * telling it to work with any type extending {@link Identifiable}. so that all basic
 * CRUD operations are already in place. also extends {@link CollectionHelper} so those methods
 * are available for any class implementing this interface.
 * <p>
 * The repository also contains helper methods for persisting and deleting entities
 * either passing in an arbitrary amount or using lists and sets. This adds some more functionality for
 * the somewhat "basic" operations in the {@link CrudRepository} that works with {@link Iterable}.
 * <p>
 * Refer to {@link CrudRepository} for more information.
 *
 * @param <ID> any {@link Object} that is used as the primary id for the {@link Identifiable} type this repository is working with
 * @param <T>  any class extending {@link Identifiable}
 * @author johan
 */
@NoRepositoryBean
public interface BaseRepository<ID, T extends Identifiable<ID>> extends CrudRepository<T, ID>, CollectionHelper<T> {

    /**
     * Same as {@link CrudRepository#saveAll(Iterable)} but instead
     * of passing in an {@link Iterable} we can pass in an arbitrary
     * amount of entities.
     *
     * @param entities the entities to persist.
     * @return the entities persisted as a List.
     */
    default List<T> saveAll(T... entities) {
        return saveAll(mListOf(entities));
    }

    /**
     * Same as {@link CrudRepository#saveAll(Iterable)} but instead
     * of passing in an {@link Iterable} we can pass in an arbitrary
     * amount of entities.
     *
     * @param entities the entities to persist.
     * @return the entities persisted as a Set.
     */
    default Set<T> saveAllAsSet(T... entities) {
        return saveAll(mSetOf(entities));
    }

    /**
     * Same as {@link CrudRepository#saveAll(Iterable)} but instead
     * of passing in an {@link Iterable} we can pass in a list of entities to persist.
     *
     * @param entities the entities to persist.
     * @return the entities persisted as a List.
     */
    default List<T> saveAll(List<T> entities) {
        entities.forEach(this::save);
        return entities;
    }

    /**
     * Same as {@link CrudRepository#saveAll(Iterable)} but instead
     * of passing in an {@link Iterable} we can pass in a set of entities to persist.
     *
     * @param entities the entities to persist.
     * @return the entities persisted as a Set.
     */
    default Set<T> saveAll(Set<T> entities) {
        entities.forEach(this::save);
        return entities;
    }

    /**
     * Same as {@link CrudRepository#deleteAll(Iterable)} but instead
     * of passing in an {@link Iterable} we can pass in an arbitrary
     * amount of entities.
     *
     * @param entities the entities to delete.
     */
    default void deleteAll(T... entities) {
        deleteAll(List.of(entities));
    }

    /**
     * Same as {@link CrudRepository#deleteAll(Iterable)} but instead
     * of passing in an {@link Iterable} we can pass in a collection.
     *
     * @param entities the entities to delete.
     */
    default void deleteAll(Collection<T> entities) {
        entities.forEach(this::delete);
    }

}
