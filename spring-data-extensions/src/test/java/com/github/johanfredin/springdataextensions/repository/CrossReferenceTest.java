package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.domain.Identifiable;

public interface CrossReferenceTest<E extends Identifiable> {

    /**
     * Creates a new entity E and populates all its children and relations.
     * The main entity and its children will not be persisted in the DB.
     *
     * @param biDirectional whether or not to have the children set their relations with their parent E as well
     * @return a fully populated entity E with all its children unpersisted in DB
     */
    E getFullyPopulatedUnpersistedEntity(boolean biDirectional);

    /**
     * Creates a new entity E and populates all its children and relations.
     * The main entity and its children willbe persisted in the DB.
     *
     * @param biDirectional whether or not to have the children set their relations with their parent E as well
     * @return a fully populated entity E with all its children persisted in DB
     */
    E getFullyPopulatedPersistedEntity(boolean biDirectional);
}
