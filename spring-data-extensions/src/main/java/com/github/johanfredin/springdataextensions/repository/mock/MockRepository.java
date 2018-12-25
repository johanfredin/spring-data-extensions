package com.github.johanfredin.springdataextensions.repository.mock;

import com.github.johanfredin.springdataextensions.domain.Identifiable;
import com.github.johanfredin.springdataextensions.repository.BaseRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.*;

/**
 * Mock implementation of {@link BaseRepository}. Useful in testing repositories connected
 * to services in controllers or just plain service tests. The mock repository saves data
 * in a {@link HashMap} instead of persisting it in a database. All methods are the same otherwise
 *
 * @author johan
 */
@NoRepositoryBean
public abstract class MockRepository<ID extends Serializable, E extends Identifiable<ID>> implements BaseRepository<ID, E> {

    protected Map<ID, E> entities;
    private long id;

    public MockRepository() {
        this.entities = new HashMap<ID, E>();
    }

    public abstract ID nextId();

    private List<E> vals() {
        return new ArrayList<>(entities.values());
    }

    private E addOrUpdate(E entity) {
        ID id = null;
        if (entity.isExistingEntity()) {
            id = entity.getId();
        } else {
            id = nextId();
            entity.setId(id);
        }
        this.entities.put(id, entity);
        return entity;
    }

    @Override
    public <S extends E> S save(S entity) {
        return (S) entities.put(nextId(), entity);
    }

    @Override
    public <S extends E> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<E> findById(ID id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(ID id) {
        return false;
    }

    @Override
    public List<E> findAll() {
        return vals();
    }

    @Override
    public Iterable<E> findAllById(Iterable<ID> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(ID id) {

    }

    @Override
    public void delete(E entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends E> entities) {

    }

    @Override
    public void deleteAll() {

    }


}
