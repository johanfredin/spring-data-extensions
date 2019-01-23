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
package com.github.johanfredin.springdataextensions;

import com.github.johanfredin.springdataextensions.util.CollectionHelper;

import java.util.List;
import java.util.Set;

/**
 * Helper interface when working with integration tests or mocked service unit tests.
 * All abstract integration and service tests in this api implement this interface.
 * Holds 2 methods {@link #getEntity1()} and {@link #getEntity2()} that you must implement
 * whereas the rest of the methods have default behaviour.
 *
 * @param <T> the type to work with
 * @author johan
 */
public interface TransactionalTest<T> extends CollectionHelper {

    /** @return an entity of type T */
    T getEntity1();

    /** @return and entity of type T */
    T getEntity2();

    /** @return a mutable list containing {@link #getEntity1()} and {@link #getEntity2()} */
    default List<T> getEntities() {
        return mListOf(getEntity1(), getEntity2());
    }

    /** @return the simple class name of type T */
    default String entityName() {
        return getEntity1().getClass().getSimpleName();
    }

    /** @return a mutable set containing {@link #getEntity1()} and {@link #getEntity2()} */
    default Set<T> getEntitiesAsSet() {
        return mSetOf(getEntity1(), getEntity2());
    }

}
