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
 * in a {@link HashMap} instead of persisting it in a database. All methods are the same otherwise
 *
 * @author johan
 */
@NoRepositoryBean
public abstract class MockRepository<ID, T extends Identifiable<ID>> implements BaseRepository<ID, T> {

    protected Map<ID, T> entities;

    public MockRepository() {
        this.entities = new HashMap<>();
    }

    public abstract ID nextId();

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
