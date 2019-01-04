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
 * @param <ID>
 * @param <T>
 */
public interface CrossReferenceHolderIntegrationTest<ID, T extends Identifiable<ID>> {

    /**
     * Creates a new entity T and populates all its children and relations.
     * The main entity and its children will not be persisted in the DB.
     *
     * @param biDirectional whether or not to have the children set their relations with their parent T as well
     * @return a fully populated entity T with all its children not persisted in DB
     */
    T getFullyPopulatedUnpersistedEntity(boolean biDirectional);

    /**
     * Creates a new entity T and populates all its children and relations.
     * The main entity and its children will be persisted in the DB.
     *
     * @param biDirectional whether or not to have the children set their relations with their parent T as well
     * @return a fully populated entity T with all its children persisted in DB
     */
    T getFullyPopulatedPersistedEntity(boolean biDirectional);
}
