package com.github.johanfredin.springdataextensions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Abstract superclass for all repositories used, extends Spring Data interfaces {@link JpaRepository} and {@link JpaSpecificationExecutor}
 *
 * @param <E> any class extending {@link AbstractEntity}
 * @author johan
 */
@NoRepositoryBean
public interface BaseRepository<E extends AbstractEntity> extends JpaRepository<E, Long>, JpaSpecificationExecutor<E> {

    default List<E> save(E... entities) {
        return save(entities);
    }

    default void delete(E... entities) {
        delete(entities);
    }
}
