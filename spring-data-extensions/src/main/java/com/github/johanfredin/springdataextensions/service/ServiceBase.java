package com.github.johanfredin.springdataextensions.service;

import com.github.johanfredin.springdataextensions.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

/**
 * Basic service to contain the basic CRUD functionality for our services.
 * Much like the {@link BaseRepository} interface. All methods here are by default
 * sent to a corresponding {@link BaseRepository} implementation class.
 *
 * @param <E> Any JPA entity extending {@link AbstractEntity}
 * @param <F> Any class extending {@link BaseRepository}
 * @author johan
 */
public interface ServiceBase<E extends AbstractEntity, F extends BaseRepository<E>> {

    /**
     * Get the repository implementation
     *
     * @return
     */
    F getRepository();

    /**
     * Set the repository to use
     *
     * @param repository any class extending {@link BaseRepository}
     */
    void setRepository(F repository);

    default E save(E entity) {
        return getRepository().save(entity);
    }

    /**
     * This method takes for granted that the entity is persisted.
     * Will update the entitys change date if so
     *
     * @param entity
     * @return a call to {@link #save(AbstractEntity)} with an updated change date
     */
    default E save(E entity, boolean updateLastCangeDate) {
        if (updateLastCangeDate) {
            if (entity != null) {
                if (entity.isExistingEntity()) {
                    entity.updateLastChangeDate();
                }
            }
        }
        return save(entity);
    }

    default E saveAndFlush(E entity) {
        return getRepository().saveAndFlush(entity);
    }

    default List<E> save(Iterable<E> entities) {
        return getRepository().saveAll(entities);
    }

    @SuppressWarnings("unchecked")
    default List<E> save(E... entities) {
        return save(Arrays.asList(entities));
    }

    default E findOne(long id) {
        return getRepository().getOne(id);
    }

    default List<E> findAllById() {
        return getRepository().findAll();
    }

    default List<E> findAllById(Iterable<Long> ids) {
        return getRepository().findAllById(ids);
    }

    default Page<E> findAllById(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    default List<E> findAllById(Sort sort) {
        return getRepository().findAll(sort);
    }

    default void delete(long id) {
        getRepository().deleteById(id);
    }

    default void delete(E entity) {
        getRepository().delete(entity);
    }

    default void delete(Iterable<? extends E> entities) {
        getRepository().deleteAll(entities);
    }

    @SuppressWarnings("unchecked")
    default void delete(E... entities) {
        delete(Arrays.asList(entities));
    }

    default void deleteInBatch(Iterable<E> entities) {
        getRepository().deleteInBatch(entities);
    }

    default boolean existsById(long id) {
        return getRepository().existsById(id);
    }

    default long count() {
        return getRepository().count();
    }

    default void flush() {
        getRepository().flush();
    }


}
