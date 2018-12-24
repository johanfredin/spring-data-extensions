package com.github.johanfredin.springdataextensions.domain;

import java.io.Serializable;

public interface Copyable<ID extends Serializable, E extends Identifiable<ID>> {

    /**
     * Intended to be used for copying the jarfields from the populatedEntity
     * Default method does the following:<br>
     * Copies the id of the passed in entity<br>
     *
     * @param populatedEntity      the {@link AbstractEntity} with the data
     */
    E createCopy(E populatedEntity);

}
