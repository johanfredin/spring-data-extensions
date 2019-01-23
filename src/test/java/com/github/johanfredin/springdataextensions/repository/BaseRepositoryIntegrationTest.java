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

import com.github.johanfredin.springdataextensions.TransactionalTest;
import com.github.johanfredin.springdataextensions.domain.Identifiable;
import com.github.johanfredin.springdataextensions.util.RepositoryUtil;
import org.junit.After;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Tests all the methods in the {@link org.springframework.data.repository.CrudRepository} as well
 * as the additional methods in {@link org.springframework.data.repository.CrudRepository}. All implementations of {@link org.springframework.data.repository.CrudRepository}
 * should extend this test class thereby reducing code duplication and making sure CRUD operations work
 * properly.
 *
 * @param <ID> any {@link Object} that is used as the primary id for the {@link Identifiable} type this service is working with
 * @param <T>  Any JPA entity extending {@link Identifiable}
 * @param <R>  Any class extending {@link org.springframework.data.repository.CrudRepository}
 * @author johan
 */
public abstract class BaseRepositoryIntegrationTest<ID, T extends Identifiable<ID>, R extends BaseRepository<ID, T>> implements TransactionalTest<T> {

    /**
     * Retrieve the {@link org.springframework.data.repository.CrudRepository} implementation that each subclass must supply
     *
     * @return the repository implementation
     */
    public abstract R getRepository();

    /**
     * Calls {@link #persistEntity1()} and {@link #persistEntity2()}
     * and then returns the persisted entities in a mutable list
     *
     * @return a mutable list with 2 persisted entities of type T
     */
    public List<T> persistEntity1And2() {
        return mListOf(persistEntity1(), persistEntity2());
    }

    /**
     * Persists entity in {@link #getEntity1()} and returns it
     *
     * @return entity1 persisted.
     */
    public T persistEntity1() {
        return getRepository().save(getEntity1());
    }

    /**
     * Persists entity in {@link #getEntity2()}  and returns it
     *
     * @return entity2 persisted.
     */
    public T persistEntity2() {
        return getRepository().save(getEntity2());
    }

    /**
     * Helper method that asserts {@link Identifiable#isPersistedEntity()} on each entry in passed in collection.
     *
     * @param collection the collection whose entities we will assert are persisted
     */
    protected void assertPersisted(Collection<T> collection) {
        collection.forEach(e -> assertEquals("Persisted", true, e.isPersistedEntity()));
    }

    /**
     * Helper method that asserts {@link org.springframework.data.repository.CrudRepository#existsById(Object)} == {@code false} on each entry in passed in collection.
     *
     * @param collection the collection whose entities we will assert are deleted in db.
     */
    protected void assertDeleted(Collection<T> collection) {
        collection.forEach(e -> assertEquals("Deleted", false, getRepository().existsById(e.getId())));
    }

    /** Default behaviour = {@link org.springframework.data.repository.CrudRepository#deleteAll()} after each test */
    @After
    public void tearDown() {
        getRepository().deleteAll();
    }

    /** Test {@link org.springframework.data.repository.CrudRepository#save(Object)} */
    @Test
    public void testSave() {
        persistEntity1();
        assertEquals("There should be one entity of type=" + entityName() + " in DB", 1, getRepository().count());
    }

    /** Test {@link org.springframework.data.repository.CrudRepository#saveAll(Iterable)} */
    @Test
    public void testSaveAll() {
        getRepository().saveAll(getEntities());
        assertEquals("There should be two entities of type=" + entityName() + " in DB", 2, getRepository().count());
    }

    /** Test {@link org.springframework.data.repository.CrudRepository#findById(Object)} */
    @Test
    public void testFindById() {
        T entity = getRepository().findById(persistEntity1().getId()).get();
        assertNotNull("Entity of type=" + entityName() + " with id=" + entity.getId() + " should exist in db", entity);
    }

    /** Test {@link org.springframework.data.repository.CrudRepository#existsById(Object)} */
    @Test
    public void testExistsById() {
        T t = persistEntity1();
        assertTrue("Entity of type=" + entityName() + " exists by id=" + t.getId(), getRepository().existsById(t.getId()));
    }

    /** Test {@link org.springframework.data.repository.CrudRepository#findAll()} */
    @Test
    public void testFindAll() {
        persistEntity1And2();
        assertEquals("findAll() should return 2 entities of type=" + entityName(), 2, getRepository().count());
    }

    /** Test {@link org.springframework.data.repository.CrudRepository#findAllById(Iterable)} */
    @Test
    public void testFindAllById() {
        List<T> entities = persistEntity1And2();
        T t1 = entities.get(0);
        T t2 = entities.get(1);

        List<T> allById = (List<T>) getRepository().findAllById(List.of(t1.getId()));
        assertEquals("findAllById() with id=" + t1.getId() + " should result in 1 match of type=" + entityName(),
                1,
                allById.size());

        allById = (List<T>) getRepository().findAllById(List.of(t1.getId(), t2.getId()));
        assertEquals("findAllById() with ids=" + RepositoryUtil.getIdsForEntity(entities) +
                        " should result in 2 matches of type=" + entityName(),
                2, allById.size());
    }

    /** Test {@link org.springframework.data.repository.CrudRepository#count()} */
    @Test
    public void testCount() {
        persistEntity1And2();
        assertEquals("calling count() should result in 2 entries of type=" + entityName() + "", 2, getRepository().count());
    }

    /** Test {@link org.springframework.data.repository.CrudRepository#deleteById(Object)} */
    @Test
    public void testDeleteById() {
        List<T> entities = persistEntity1And2();
        assertEquals("There should be two entities of type=" + entityName() + " in DB", 2, getRepository().count());
        T t1 = entities.get(0);
        T t2 = entities.get(1);
        getRepository().deleteById(t1.getId());
        assertEquals("After deleting t1 there should be 1 entity left of type=" + entityName(), 1, getRepository().count());
    }

    /** Test {@link org.springframework.data.repository.CrudRepository#delete(Object)} */
    @Test
    public void testDelete() {
        List<T> entities = persistEntity1And2();
        assertEquals("There should be two entities of type=" + entityName() + " in DB", 2, getRepository().count());
        T t1 = entities.get(0);
        T t2 = entities.get(1);
        getRepository().delete(t1);
        assertEquals("After deleting t1 there should be 1 entity left of type=" + entityName(), 1, getRepository().count());
    }

    /** Test {@link org.springframework.data.repository.CrudRepository#deleteAll(Iterable)} */
    @Test
    public void testDeleteAllIterable() {
        List<T> entities = persistEntity1And2();
        assertEquals("There should be two entities of type=" + entityName() + " in DB", 2, getRepository().count());
        T t1 = entities.get(0);
        T t2 = entities.get(1);
        getRepository().deleteAll(List.of(t1, t2));
        assertEquals("After deleting t1 there should be 0 entities left of type=" + entityName(), 0, getRepository().count());
    }

    /** Test {@link org.springframework.data.repository.CrudRepository#deleteAll()} */
    @Test
    public void testDeleteAll() {
        persistEntity1And2();
        getRepository().deleteAll();
        assertEquals("There should be 0 entities of type=" + entityName() + " in DB after deleteAll()", 0, getRepository().count());
    }

}
