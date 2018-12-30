package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.domain.Identifiable;
import com.github.johanfredin.springdataextensions.util.RepositoryUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Tests all the methods in the {@link org.springframework.data.repository.CrudRepository} as well
 * as the additional methods in {@link BaseRepository}. All implementations of {@link BaseRepository}
 * should extend this test class thereby reducing code duplication and making sure CRUD operations work
 * properly.
 *
 * @author johan
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public abstract class BaseRepositoryTest<ID, T extends Identifiable<ID>, R extends BaseRepository<ID, T>> {

    public Logger log = LogManager.getLogger(this.getClass());

    public abstract R getRepository();

    public abstract T getEntity1();

    public abstract T getEntity2();

    public List<T> getEntities() {
        return getRepository().modifiableList(getEntity1(), getEntity2());
    }

    public String entityName() {
        return getEntity1().getClass().getSimpleName();
    }

    public T persistEntity1() {
        return getRepository().save(getEntity1());
    }

    public T persistEntity2() {
        return getRepository().save(getEntity2());
    }

    public List<T> persistEntity1And2() {
        return getRepository().save(getEntity1(), getEntity2());
    }


    @After
    public void tearDown() {
        getRepository().deleteAll();
    }

    @Test
    public void testSave() {
        persistEntity1();
        assertEquals("There should be one entity of type=" + entityName() + " in DB", 1, getRepository().count());
    }

    @Test
    public void testSaveAll() {
        getRepository().saveAll(getEntities());
        assertEquals("There should be two entities of type=" + entityName() + " in DB", 2, getRepository().count());
    }

    @Test
    public void testFindById() {
        T t = getEntity1();
        assertFalse("Entity of type=" + entityName() + " should not be an existing entity", t.isExistingEntity());
        getRepository().save(t);
        assertTrue("Entity of type=" + entityName() + " should be an existing entity", t.isExistingEntity());
        T tInDB = getRepository().findById(t.getId()).get();
        assertEquals("Entity of type=" + entityName() + " found in db should match the persisted entity", tInDB, t);
    }

    @Test
    public void testExistsById() {
        T t = persistEntity1();
        assertTrue("Entity of type=" + entityName() + " exists by id=" + t.getId(), getRepository().existsById(t.getId()));
    }

    @Test
    public void testFindAll() {
        persistEntity1And2();
        assertEquals("findAll() should return 2 entities of type=" + entityName(), 2, getRepository().count());
    }

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

    @Test
    public void testCount() {
        persistEntity1And2();
        assertEquals("calling count() should result in 2 entries of type=" + entityName() + "", 2, getRepository().count());
    }

    @Test
    public void testDeleteById() {
        List<T> entities = persistEntity1And2();
        assertEquals("There should be two entities of type=" + entityName() + " in DB", 2, getRepository().count());
        T t1 = entities.get(0);
        T t2 = entities.get(1);
        getRepository().deleteById(t1.getId());
        assertEquals("After deleting t1 there should be 1 entity left of type=" + entityName(), 1, getRepository().count());
    }

    @Test
    public void testDelete() {
        List<T> entities = persistEntity1And2();
        assertEquals("There should be two entities of type=" + entityName() + " in DB", 2, getRepository().count());
        T t1 = entities.get(0);
        T t2 = entities.get(1);
        getRepository().delete(t1);
        assertEquals("After deleting t1 there should be 1 entity left of type=" + entityName(), 1, getRepository().count());
    }

    @Test
    public void testDeleteAllIterable() {
        List<T> entities = persistEntity1And2();
        assertEquals("There should be two entities of type=" + entityName() + " in DB", 2, getRepository().count());
        T t1 = entities.get(0);
        T t2 = entities.get(1);
        getRepository().deleteAll(List.of(t1, t2));
        assertEquals("After deleting t1 there should be 0 entities left of type=" + entityName(), 0, getRepository().count());
    }

    @Test
    public void testDeleteAll() {
        persistEntity1And2();
        getRepository().deleteAll();
        assertEquals("There should be 0 entities of type=" + entityName() + " in DB after deleteAll()", 0, getRepository().count());
    }

}
