package com.github.johanfredin.springdataextensions.domain;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public interface Copyable<ID, E extends Identifiable<ID>> {

    /**
     * Populates our entity with the data of the passed in entity.
     * What fields and references to populate from must be decided in the implementation.
     * @param populatedEntity the entity with data we want to copy (must not be null!)
     */
    void copyFrom(@NotNull E populatedEntity);

    /**
     * Creates a new instance and assigns it all fields and references we want
     *
     * @return a new instance populated with the data the owning entity already possess.
     */
    E createCopy();

}
