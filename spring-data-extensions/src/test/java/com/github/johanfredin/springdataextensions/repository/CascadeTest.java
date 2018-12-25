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
