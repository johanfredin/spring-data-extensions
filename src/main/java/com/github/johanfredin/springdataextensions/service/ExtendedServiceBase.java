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
package com.github.johanfredin.springdataextensions.service;

import com.github.johanfredin.springdataextensions.domain.ChangeDateHolder;
import com.github.johanfredin.springdataextensions.domain.Identifiable;
import com.github.johanfredin.springdataextensions.repository.BaseRepository;
import com.github.johanfredin.springdataextensions.util.CollectionHelper;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Extension of {@link ServiceBase} that also holds a couple of extra helpful methods
 * for persisting and deleting entities by passing in and returning Lists, Sets and arbitrary amount of entities
 * instead of the rather primitive {@link CrudRepository} where we can "only" work with {@link Iterable}.
 * Check out these methods for more info:<br/>
 * {@link #saveAll(Set)}<br/>
 * {@link #saveAll(List)}<br/>
 * {@link #saveAll(Identifiable[])}<br/>
 * {@link #saveAllAsSet(Identifiable[])}<br/>
 * <p>
 * If our {@link Identifiable} also implements {@link ChangeDateHolder} we have methods for persisting and automatically
 * updating the change date field of the entity.
 * Check out these methods for more info:<br>
 * {@link #save(Identifiable, boolean)}<br/>
 * {@link #saveAll(boolean, Identifiable[])}<br/>
 * {@link #saveAll(Set, boolean)}<br/>
 * {@link #saveAllAsSet(boolean, Identifiable[])}<br/>
 * <p>
 * Also extends {@link CollectionHelper} so that those methods become available here as well.
 *
 * @param <ID> any {@link Object} that is used as the primary id for the {@link Identifiable} type this service is working with
 * @param <T>  Any JPA entity extending {@link Identifiable}
 * @param <R>  Any class extending {@link BaseRepository}
 * @author johan
 */
public interface ExtendedServiceBase<ID, T extends Identifiable<ID>, R extends BaseRepository<ID, T>> extends ServiceBase<ID, T, R>, CollectionHelper<T> {

    /**
     * If our {@link Identifiable} entity also implements {@link ChangeDateHolder}
     * we have the option to update the {@link ChangeDateHolder#getLastChangeDate()} field.
     * After update is done a call to {@link CrudRepository#save(Object)} is made persisting
     * the entity as usual.
     *
     * @param entity               the entity to persist/merge
     * @param updateLastChangeDate whether or not to update the last change date (only works when entity implements {@link ChangeDateHolder} interface.
     * @return the persisted entity.
     */
    default T save(T entity, boolean updateLastChangeDate) {
        if (updateLastChangeDate && entity instanceof ChangeDateHolder) {
            ((ChangeDateHolder) entity).updateLastChangeDate();
        }
        return save(entity);
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


    /**
     * Iterate an arbitrary amount of passed in entities and call {@link #save(Identifiable, boolean)}
     * on each entity
     *
     * @param updateLastChangeDate whether or not to update the last change date (only works when entity implements {@link ChangeDateHolder} interface.
     * @param entities             the entities to persist.
     * @return the entities persisted as a List.
     */
    default List<T> saveAll(boolean updateLastChangeDate, T... entities) {
        return mListOf(entities)
                .stream()
                .peek(e -> save(e, updateLastChangeDate))
                .collect(Collectors.toList());
    }

    /**
     * Iterate passed in entities and call {@link #save(Identifiable, boolean)}
     * on each entity
     *
     * @param updateLastChangeDate whether or not to update the last change date (only works when entity implements {@link ChangeDateHolder} interface.
     * @param entities             the entities to persist.
     * @return the entities persisted as a Set.
     */
    default Set<T> saveAllAsSet(boolean updateLastChangeDate, T... entities) {
        return mSetOf(entities)
                .stream()
                .peek(e -> save(e, updateLastChangeDate))
                .collect(Collectors.toSet());
    }

    /**
     * Iterate passed in entities and call {@link #save(Identifiable, boolean)}
     * on each entity
     *
     * @param entities             the entities to persist.
     * @param updateLastChangeDate whether or not to update the last change date (only works when entity implements {@link ChangeDateHolder} interface.
     * @return the entities persisted as a List.
     */
    default List<T> saveAll(List<T> entities, boolean updateLastChangeDate) {
        entities.forEach(e -> save(e, updateLastChangeDate));
        return entities;
    }

    /**
     * Iterate passed in entities and call {@link #save(Identifiable, boolean)}
     * on each entity
     *
     * @param entities             the entities to persist.
     * @param updateLastChangeDate whether or not to update the last change date (only works when entity implements {@link ChangeDateHolder} interface.
     * @return the entities persisted as a Set.
     */
    default Set<T> saveAll(Set<T> entities, boolean updateLastChangeDate) {
        entities.forEach(e -> save(e, updateLastChangeDate));
        return entities;
    }


}
