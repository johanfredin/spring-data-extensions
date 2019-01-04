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
package com.github.johanfredin.springdataextensions.util;

import java.util.*;

/**
 * Holds some helper methods for working with collections. Set and List
 * specifically. Starting from java 9 the awesome {@link List#of()}, {@link Set#of()}
 * methods were added. However these create immutable collections that are not always what
 * we want to work with. This interface aims to have the same ease of use but instead creates
 * mutable collections.
 * @param <T> the type the collection will contain.
 */
public interface CollectionHelper<T> {

    /**
     * Creates a mutable list of the entities passed in.
     * @param entities the entities to put in the list
     * @return the entities passes in as a mutable list.
     */
    default List<T> mListOf(T... entities) {
        return mListOf(List.of(entities));
    }

    /**
     * Creates a mutable list of the entities passed in.
     * @param list the entities to put in the list
     * @return the entities passes in as a mutable list.
     */
    default List<T> mListOf(Collection<T> list) {
        return new ArrayList<>(list);
    }

    /**
     * Creates a mutable set of the entities passed in.
     * @param entities the entities to put in the set
     * @return the entities passes in as a mutable set.
     */
    default Set<T> mSetOf(T... entities) {
        return mSetOf(Set.of(entities));
    }

    /**
     * Creates a mutable set of the entities passed in.
     * @param entities the entities to put in the set
     * @return the entities passes in as a mutable set.
     */
    default Set<T> mSetOf(Set<T> entities) {
        return new HashSet<T>(entities);
    }

}
