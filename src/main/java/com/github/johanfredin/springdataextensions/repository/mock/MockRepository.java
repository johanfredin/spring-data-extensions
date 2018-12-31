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
package com.github.johanfredin.springdataextensions.repository.mock;

import com.github.johanfredin.springdataextensions.domain.Identifiable;
import com.github.johanfredin.springdataextensions.repository.BaseRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Mock implementation of {@link BaseRepository}. Useful in testing repositories connected
 * to services in controllers or just plain service tests. The mock repository saves data
 * in a {@link HashMap} instead of persisting it in a database where map key={@link Identifiable#getId()} and value={@link Identifiable}.
 * All methods are the same otherwise. Refer to {@link org.springframework.data.repository.CrudRepository} and {@link BaseRepository}
 * for more documentation.
 *
 * @param <ID> any {@link Object} that is used as the primary id for the {@link Identifiable} type this repository is working with
 * @param <T> any class extending {@link Identifiable}
 *
 * @author johan
 */
@NoRepositoryBean
public abstract class MockRepository<ID, T extends Identifiable<ID>> implements BaseRepository<ID, T> {

    private Map<ID, T> entities;

    /**
     * Creates a new instance instantiating the private Map used instead of a DB connection.
     * Subclasses with their own constructors must call super() on this.
     */
    public MockRepository() {
        this.entities = new HashMap<>();
    }

    /**
     * Must be implemented in subclass and match how that class has implemented {@link Identifiable#isPersistedEntity()} method.
     * Is privately called in the persist methods here to decide whether to persist or merge an entity.
     * @return a new unique identifier for a new entity of type T to persist.
     */
    public abstract ID nextId();

    /**
     * Called in every persist method in this interface so that we know if we are to persist or merge an object.
     * @param entity the entity to persist
     * @return the entity persisted.
     */
    private T addOrUpdate(T entity) {
        ID id = null;
        if (entity.isPersistedEntity()) {
            id = entity.getId();
        } else {
            id = nextId();
            entity.setId(id);
        }
        this.entities.put(id, entity);
        return entity;
    }


    @Override
    public <S extends T> S save(S entity) {
        return (S) addOrUpdate(entity);
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        entities.forEach(this::addOrUpdate);
        return entities;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.of(entities.get(id));
    }

    @Override
    public boolean existsById(ID id) {
        return entities.containsKey(id);
    }

    @Override
    public Iterable<T> findAll() {
        return entities.values();
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        List<ID> idList = (List<ID>) ids;
        return entities.values()
                .stream()
                .filter(e -> idList.contains(e.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return entities.size();
    }

    @Override
    public void deleteById(ID id) {
        entities.remove(id);
    }

    @Override
    public void delete(T entity) {
        this.entities.entrySet().removeIf(k -> k.getValue().equals(entity));
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        var list = (List<T>) entities;
        this.entities.entrySet().removeIf(k -> list.contains(k.getValue()));
    }

    @Override
    public void deleteAll() {
        entities.clear();
    }

}
