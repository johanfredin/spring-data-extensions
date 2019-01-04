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

public interface CascadeTest {

    /**
     * Test to make sure references that are marked with Cascade.PERSIST gets persisted
     * when owning entity is persisted
     */
    void testCascadePersist();

    /**
     * Test to make sure references that are marked with Cascade.MERGE gets merged
     * when owning entity is merged
     */
    void testCascadeMerge();

    /**
     * Test to make sure references that are marked with Cascade.DELETE gets deleted
     * when owning entity is deleted
     */
    void testCascadeDelete();

}
