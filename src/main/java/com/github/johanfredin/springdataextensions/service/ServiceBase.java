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

import com.github.johanfredin.springdataextensions.domain.Identifiable;
import com.github.johanfredin.springdataextensions.repository.BaseRepository;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Abstract service layer. By convention repositories should not be directly exposed
 * to Controllers and web layers. They should call services only and those services should
 * redirect to the repository layer. This interface (by default) holds all the methods that
 * the {@link BaseRepository} holds. All methods have default implementations except one called
 * {@link #getRepository()} that implementing classes must define to let this service know what
 * {@link BaseRepository} implementation we need to call.
 * <p>
 * All the default methods have the same name and will by default call
 * the corresponding repository method.<br/>
 * e.g Service.save(T type) will call {@link #getRepository()#save(Identifiable)}.
 *
 * @param <ID> any {@link Object} that is used as the primary id for the {@link Identifiable} type this service is working with
 * @param <T>  Any JPA entity extending {@link Identifiable}
 * @param <R>  Any class extending {@link BaseRepository}
 * @author johan
 */
public interface ServiceBase<ID, T extends Identifiable<ID>, R extends BaseRepository<ID, T>> {

    // ************************************************************************************
    // -                    FROM CRUD-REPOSITORY INTERFACE                                -
    // ************************************************************************************

    /**
     * Get the repository implementation
     *
     * @return the {@link BaseRepository} implementation this service will call.
     */
    R getRepository();

    default T save(T entity) {
        return getRepository().save(entity);
    }

    /**
     * Refer to {@link org.springframework.data.repository.CrudRepository#saveAll(Iterable)}
     *
     * @param entities the entities to persist
     * @return the persisted entities
     */
    default Iterable<T> saveAll(Iterable<T> entities) {
        return getRepository().saveAll(entities);
    }

    /**
     * Refer to {@link org.springframework.data.repository.CrudRepository#findById(Object)}
     *
     * @param id the identifier of the entity to find
     * @return an optional containing a found entity or empty optional.
     */
    default Optional<T> findById(@NotNull ID id) {
        return getRepository().findById(id);
    }

    /**
     * Same as {@link #findById(Object)} but returns an instance of T and not an {@link Optional}.
     *
     * @param id the identifier of the entity to find
     * @return the entity with given id
     * @throws IllegalStateException if not entity found with that id.
     */
    default T getEntityById(@NotNull ID id) {
        return findById(id).orElseThrow(IllegalStateException::new);
    }

    /**
     * Refer to {@link org.springframework.data.repository.CrudRepository#existsById(Object)}
     *
     * @param id the identifier of the entity to check if persisted or not
     * @return true if an entity with passed in id exists.
     */
    default boolean existsById(ID id) {
        return getRepository().existsById(id);
    }

    /**
     * Refer to {@link CrudRepository#findAll()}
     *
     * @return all entities of type T in database or an empty collection.
     */
    default Iterable<T> findAll() {
        return getRepository().findAll();
    }

    /**
     * Refer to {@link CrudRepository#findAllById(Iterable)}
     *
     * @param ids the identifiers of entities to find
     * @return all entities matching the identifiers passed in or an empty collection if none found.
     */
    default Iterable<T> findAllById(Iterable<ID> ids) {
        return getRepository().findAllById(ids);
    }

    /**
     * Refer to {@link CrudRepository#count()}
     *
     * @return the amount of entities in database.
     */
    default long count() {
        return getRepository().count();
    }

    /**
     * Refer to {@link CrudRepository#deleteById(Object)}
     *
     * @param id the identifier of the entity to delete.
     */
    default void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    /**
     * Refer to {@link CrudRepository#delete(Object)}
     *
     * @param entity the entity to delete
     */
    default void delete(T entity) {
        getRepository().delete(entity);
    }

    /**
     * Refer to {@link CrudRepository#deleteAll(Iterable)}
     *
     * @param entities the entities to delete
     */
    default void deleteAll(Iterable<T> entities) {
        getRepository().deleteAll(entities);
    }

    /**
     * Refer to {@link CrudRepository#deleteAll()}
     */
    default void deleteAll() {
        getRepository().deleteAll();
    }


}
