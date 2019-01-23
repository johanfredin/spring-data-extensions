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
package com.github.johanfredin.springdataextensions.service;

import com.github.johanfredin.springdataextensions.domain.Identifiable;
import com.github.johanfredin.springdataextensions.repository.ExtendedBaseRepository;

import java.util.List;
import java.util.Set;


/**
 * Extension of {@link ServiceBase} that also holds service wrapper methods for all methods in the {@link ExtendedBaseRepository}
 * Just like in the ServiceBase interface, all methods exists here under the same name as in the corresponding repository.
 * Check out {@link ExtendedBaseRepository} for more documentation.
 *
 * @param <ID> any {@link Object} that is used as the primary id for the {@link Identifiable} type this service is working with
 * @param <T>  Any JPA entity extending {@link Identifiable}
 * @param <R>  Any class extending {@link ExtendedBaseRepository}
 * @author johan
 */
public interface ExtendedServiceBase<ID, T extends Identifiable<ID>, R extends ExtendedBaseRepository<ID, T>> extends ServiceBase<ID, T, R> {

    /**
     * Refer to {@link ExtendedBaseRepository#saveAll(Identifiable[])}
     *
     * @param entities the entities to persist.
     * @return the entities persisted as a List.
     */
    default List<T> saveAll(T... entities) {
        return getRepository().saveAll(entities);
    }

    /**
     * Refer to {@link ExtendedBaseRepository#saveAllAsSet(Identifiable[])}
     *
     * @param entities the entities to persist.
     * @return the entities persisted as a Set.
     */
    default Set<T> saveAllAsSet(T... entities) {
        return getRepository().saveAllAsSet(entities);
    }

    /**
     * Refer to {@link ExtendedBaseRepository#saveAll(List)}
     *
     * @param entities the entities to persist.
     * @return the entities persisted as a List.
     */
    default List<T> saveAll(List<T> entities) {
        return getRepository().saveAll(entities);
    }

    /**
     * Refer to {@link ExtendedBaseRepository#saveAll(Set)}
     *
     * @param entities the entities to persist.
     * @return the entities persisted as a Set.
     */
    default Set<T> saveAll(Set<T> entities) {
        return getRepository().saveAll(entities);
    }

    /**
     * Refer to {@link ExtendedBaseRepository#deleteAll(Identifiable[])}
     * @param entities arbitrary amount of entities to delete.
     */
    default void deleteAll(T... entities) {
        getRepository().deleteAll(entities);
    }

}
