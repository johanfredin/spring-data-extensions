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
package com.github.johanfredin.springdataextensions.domain;

/**
 * Common ground for all JPA entities is that they have one or several fields (but at least one primary)
 * telling JPA what is the identifier. The spring data crud repository also requires this information.
 * This interface holds properties for those fields as well as a boolean method that lets us decide what
 * qualifies as a persisted entity.
 * @param <ID> any {@link Object} that is used as the primary id in the class implementing this interface.
 */
public interface Identifiable<ID> {

    /**
     * Get the identifier field
     * @return the id field.
     */
    ID getId();

    /**
     * Set the identifier field
     * @param id the id field to set
     */
    void setId(ID id);

    /**
     * Our way to tell if this is a persisted entity or not.
     * E.g when id is a numeric field and is auto incremented this could return true if id is greater than zero.
     * Or if this is a string then this could be true if the string is not null etc.
     * @return whether or not this entity is a persisted entity.
     */
    boolean isPersistedEntity();
}
