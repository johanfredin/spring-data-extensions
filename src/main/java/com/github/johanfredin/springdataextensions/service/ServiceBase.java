package com.github.johanfredin.springdataextensions.service;

import com.github.johanfredin.springdataextensions.domain.ChangeDateHolder;
import com.github.johanfredin.springdataextensions.domain.Identifiable;
import com.github.johanfredin.springdataextensions.repository.BaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Basic service to contain the basic CRUD functionality for our services.
 * Much like the {@link BaseRepository} interface. All methods here are by default
 * sent to a corresponding {@link BaseRepository} implementation class.
 *
 * @param <T> Any JPA entity extending {@link Identifiable}
 * @param <R> Any class extending {@link BaseRepository}
 * @author johan
 */
public interface ServiceBase<ID, T extends Identifiable<ID>, R extends BaseRepository<ID, T>> {

    /**
     * Get the repository implementation
     *
     * @return
     */
    R getRepository();

    /**
     * This method takes for granted that the entity is persisted.
     * Will update the entitys change date if so
     *
     * @param entity
     * @return a call to {@link #save(Identifiable)} with an updated change date
     */
    default T save(T entity, boolean updateLastCangeDate) {
        if (entity != null && 
                entity instanceof ChangeDateHolder &&
                updateLastCangeDate &&
                entity.isExistingEntity()) {
            ((ChangeDateHolder) entity).updateLastChangeDate();
        }
        return save(entity);
    }

    default T save(T entity) {
        return getRepository().save(entity);
    }

    default Iterable<T> saveAll(Iterable<T> entities) {
        return getRepository().saveAll(entities);
    }

    default Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }

    default boolean existsById(ID id) {
        return getRepository().existsById(id);
    }

    default Iterable<T> findAll() {
        return getRepository().findAll();
    }

    default Iterable<T> findAllById(Iterable<ID> ids) {
        return getRepository().findAllById(ids);
    }

    default long count() {
        return getRepository().count();
    }

    default void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    default void delete(T entity) {
        getRepository().delete(entity);
    }

    default void deleteAll(Iterable<T> entities) {
        getRepository().deleteAll(entities);
    }

    default void deleteAll() {
        getRepository().deleteAll();
    }

    default List<T> save(T... entities) {
        return saveAll(new ArrayList<>(List.of(entities)));
    }

    default void delete(T... entities) {
        deleteAll(List.of(entities));
    }

    default List<T> saveAll(List<T> entities) {
        return saveAll(entities);
    }
}
