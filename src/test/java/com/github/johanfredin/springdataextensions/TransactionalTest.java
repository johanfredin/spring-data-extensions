package com.github.johanfredin.springdataextensions;

import com.github.johanfredin.springdataextensions.util.CollectionHelper;

import java.util.List;

public interface TransactionalTest<T> extends CollectionHelper {

    T getEntity1();

    T getEntity2();

    default List<T> getEntities() {
        return mListOf(getEntity1(), getEntity2());
    }

    default String entityName() {
        return getEntity1().getClass().getSimpleName();
    }

}
