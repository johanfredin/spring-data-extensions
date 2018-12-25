package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.domain.Identifiable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Abstract superclass for all repositories used, extends Spring Data interfaces {@link JpaRepository} and {@link JpaSpecificationExecutor}
 *
 * @author johan
 */
@NoRepositoryBean
public interface BaseRepository<ID extends Serializable, E extends Identifiable<ID>> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {

    default List<E> save(E... entities) {
        return save(entities);
    }

    default void delete(E... entities) {
        delete(entities);
    }
}
