package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.domain.Identifiable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract superclass for all repositories used, extends Spring Data interfaces
 *
 * @author johan
 */
@NoRepositoryBean
public interface BaseRepository<ID, T extends Identifiable<ID>> extends CrudRepository<T, ID> {

    default List<T> save(T... entities) {
        return saveAll(modifiableList(entities));
    }

    default void delete(T... entities) {
        deleteAll(modifiableList(entities));
    }

    default List<T> saveAll(List<T> entities) {
        entities.forEach(this::save);
        return entities;
    }

    default List<T> modifiableList(T... entities) {
        return modifiableList(List.of(entities));
    }

    default List<T> modifiableList(List<T> list) {
        return new ArrayList<>(list);
    }
}
