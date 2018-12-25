package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.domain.Identifiable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract superclass for all repositories used, extends Spring Data interfaces {@link JpaRepository} and {@link JpaSpecificationExecutor}
 *
 * @author johan
 */
@NoRepositoryBean
public interface BaseRepository<ID extends Serializable, E extends Identifiable<ID>> extends CrudRepository<E, ID> {

    default List<E> save(E... entities) {
        return saveAll(modifiableList(entities));
    }

    default void delete(E... entities) {
        deleteAll(modifiableList(entities));
    }

    default List<E> saveAll(List<E> entities) {
        entities.forEach(this::save);
        return entities;
    }

    default List<E> modifiableList(E... entities) {
        return modifiableList(List.of(entities));
    }

    default List<E> modifiableList(List<E> list) {
        return new ArrayList<>(list);
    }
}
