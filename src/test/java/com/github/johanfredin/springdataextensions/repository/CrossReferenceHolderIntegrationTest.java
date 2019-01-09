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

import com.github.johanfredin.springdataextensions.domain.CrossReferenceHolder;
import com.github.johanfredin.springdataextensions.domain.Identifiable;

/**
 * When working with entities that have cross relations (e.g {@link CrossReferenceHolder})
 * we want to
 * @param <ID> any {@link Object} that is used as the primary id in the class implementing this interface.
 * @param <T> any class extending {@link Identifiable}
 * @author johan
 */
public interface CrossReferenceHolderIntegrationTest<ID, T extends Identifiable<ID>> {

    /**
     * Intended to create a new entity T and populate all its children and relations.
     * The main entity and its children should not be persisted in the DB.
     *
     * @param biDirectional whether or not to have the children set their relations with their parent T as well
     * @return a fully populated entity T with all its children not persisted in DB
     */
    T getFullyPopulatedUnpersistedEntity(boolean biDirectional);

    /**
     * Intended to create a new entity T and populate all its children and relations.
     * The main entity and its children should be persisted in the DB.
     *
     * @param biDirectional whether or not to have the children set their relations with their parent T as well
     * @return a fully populated entity T with all its children persisted in DB
     */
    T getFullyPopulatedPersistedEntity(boolean biDirectional);
}
