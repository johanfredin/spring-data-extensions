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

import javax.validation.constraints.NotNull;

/**
 * Used when we want to create custom copies of our entities. Either transferring
 * data from an already populated entity or creating a copy to another entity.
 * @param <T> the type to copy
 */
public interface Copyable<T> {

    /**
     * Populates our entity with the data of the passed in entity.
     * What fields and references to populate from must be decided in the implementation.
     * @param populatedEntity the entity with data we want to copy (must not be null!)
     */
    void copyFrom(@NotNull T populatedEntity);

    /**
     * Creates a new instance and assigns it all fields and references we want
     *
     * @return a new instance populated with the data the owning entity already possess.
     */
    T createCopy();

}
