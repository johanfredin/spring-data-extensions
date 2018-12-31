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
