package com.github.johanfredin.springdataextensions.repository.mock;

import com.github.johanfredin.springdataextensions.repository.BaseRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.*;

/**
 * Mock implementation of {@link BaseRepository}. Useful in testing repositories connected
 * to services in controllers or just plain service tests. The mock repository saves data
 * in a {@link HashMap} instead of persisting it in a database. All methods are the same otherwise
 *
 * @param <E> any class extending  {@link AbstractEntity}
 * @author johan
 */
@NoRepositoryBean
public abstract class MockRepository<E extends AbstractEntity> implements BaseRepository<E> {

    protected Map<Long, E> entities;
    private long id;

    public MockRepository() {
        this.entities = new HashMap<Long, E>();
    }

    private long nextId() {
        return ++id;
    }

    private List<E> vals() {
        return new ArrayList<>(entities.values());
    }

    private E addOrUpdate(E entity) {
        long id = 0L;
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
    public List<E> findAll() {
        return vals();
    }

    @Override
    public List<E> findAll(Sort sort) {
        return vals();
    }

    @Override
    public List<E> findAllById(Iterable<Long> ids) {
        return null;
    }

    @Override
    public <S extends E> List<S> saveAll(Iterable<S> entities) {
        List<S> saves = new ArrayList<>();
        for (S entity : entities) {
            save(entity);
        }
        return saves;
    }

    @Override
    public Optional<E> findById(Long Id) {
        return Optional.of(entities.get(id));
    }

    @Override
    public void flush() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S extends E> S saveAndFlush(S entity) {
        return (S) addOrUpdate(entity);
    }

    @Override
    public void deleteInBatch(Iterable<E> entities) {
    }

    @Override
    public void deleteAllInBatch() {
        this.entities.clear();
    }

    @Override
    public E getOne(Long id) {
        return this.entities.get(id);
    }

    @Override
    public <S extends E> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends E> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends E> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends E> S save(S entity) {
        return (S) addOrUpdate(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return this.entities.containsKey(id);
    }

    @Override
    public long count() {
        return this.entities.size();
    }

    @Override
    public void deleteById(Long id) {
        this.entities.remove(id);
    }

    @Override
    public void delete(E entity) {
        this.entities.remove(entity.getId());
    }

    @Override
    public void deleteAll(Iterable<? extends E> entities) {
        for (E entity : entities) {
            this.entities.remove(entity.getId());
        }
    }

    @Override
    public void deleteAll() {
        this.entities.clear();
    }

    @Override
    public <S extends E> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends E> long count(Example<S> example) {
        return this.entities.size();
    }

    @Override
    public <S extends E> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public Optional<E> findOne(Specification<E> spec) {
        return null;
    }

    @Override
    public List<E> findAll(Specification<E> spec) {
        return null;
    }

    @Override
    public Page<E> findAll(Specification<E> spec, Pageable pageable) {
        return null;
    }

    @Override
    public List<E> findAll(Specification<E> spec, Sort sort) {
        return null;
    }

    @Override
    public long count(Specification<E> spec) {
        return this.entities.size();
    }

}
