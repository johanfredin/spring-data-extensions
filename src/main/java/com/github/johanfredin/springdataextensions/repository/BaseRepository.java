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
