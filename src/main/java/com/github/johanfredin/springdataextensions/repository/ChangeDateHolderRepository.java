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

import com.github.johanfredin.springdataextensions.domain.ChangeDateHolder;
import com.github.johanfredin.springdataextensions.domain.Identifiable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Extension of the {@link BaseRepository} with helper methods for persisting and deleting entities
 * either passing in an arbitrary amount or using lists and sets. This adds some more functionality for
 * the somewhat "basic" operations in the {@link CrudRepository} that works with {@link Iterable}.
 * If you are going to work with {@link org.springframework.data.jpa.repository.JpaRepository} or some
 * more advanced spring data interfaces then you probably wont need this interface.
 *
 * @param <ID> any {@link Object} that is used as the primary id for the {@link Identifiable} type this repository is working with
 * @param <T>  any class extending {@link Identifiable}
 * @author johan
 */
@NoRepositoryBean
public interface ChangeDateHolderRepository<ID, T extends ChangeDateHolder<ID>> extends BaseRepository<ID, T> {

    default T save(T entity, boolean updateLastChangeDate) {
        if(updateLastChangeDate) {
            entity.updateLastChangeDate();
        }
        return save(entity);
    }

    @Override
    default <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        entities.forEach(e -> save(e, true));
        return entities;
    }

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
        entities.forEach(e -> save(e, true));
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
        entities.forEach(e -> save(e, true));
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
