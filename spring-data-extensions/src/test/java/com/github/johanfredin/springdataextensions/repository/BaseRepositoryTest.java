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

import java.io.Serializable;
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
public abstract class BaseRepositoryTest<ID extends Serializable, E extends Identifiable<ID>, R extends BaseRepository<ID, E>> {

    public Logger log = LogManager.getLogger(this.getClass());

    public abstract R getRepository();

    public abstract E getEntity1();

    public abstract E getEntity2();

    public List<E> getEntities() {
        return getRepository().modifiableList(getEntity1(), getEntity2());
    }

    public String entityName() {
        return getEntity1().getClass().getSimpleName();
    }

    public E persistEntity1() {
        return getRepository().save(getEntity1());
    }

    public E persistEntity2() {
        return getRepository().save(getEntity2());
    }

    public List<E> persistEntity1And2() {
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
        E e = getEntity1();
        assertFalse("Entity of type=" + entityName() + " should not be an existing entity", e.isExistingEntity());
        getRepository().save(e);
        assertTrue("Entity of type=" + entityName() + " should be an existing entity", e.isExistingEntity());
        E eInDB = getRepository().findById(e.getId()).get();
        assertEquals("Entity of type=" + entityName() + " found in db should match the persisted entity", eInDB, e);
    }

    @Test
    public void testExistsById() {
        E e = persistEntity1();
        assertTrue("Entity of type=" + entityName() + " exists by id=" + e.getId(), getRepository().existsById(e.getId()));
    }

    @Test
    public void testFindAll() {
        persistEntity1And2();
        assertEquals("findAll() should return 2 entities of type=" + entityName(), 2, getRepository().count());
    }

    @Test
    public void testFindAllById() {
        List<E> entities = persistEntity1And2();
        E e1 = entities.get(0);
        E e2 = entities.get(1);
        assertEquals("findAllById() with id=" + e1.getId() + " should result in 1 match of type=" + entityName(),
                1,
                List.of(getRepository().findAllById(List.of(e1.getId()))).size());

        List<E> allById = (List<E>) getRepository().findAllById(List.of(e1.getId(), e2.getId()));
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
        List<E> entities = persistEntity1And2();
        assertEquals("There should be two entities of type=" + entityName() + " in DB", 2, getRepository().count());
        E e1 = entities.get(0);
        E e2 = entities.get(1);
        getRepository().deleteById(e1.getId());
        assertEquals("After deleting e1 there should be 1 entity left of type=" + entityName(), 1, getRepository().count());
    }

    @Test
    public void testDelete() {
        List<E> entities = persistEntity1And2();
        assertEquals("There should be two entities of type=" + entityName() + " in DB", 2, getRepository().count());
        E e1 = entities.get(0);
        E e2 = entities.get(1);
        getRepository().delete(e1);
        assertEquals("After deleting e1 there should be 1 entity left of type=" + entityName(), 1, getRepository().count());
    }

    @Test
    public void testDeleteAllIterable() {
        List<E> entities = persistEntity1And2();
        assertEquals("There should be two entities of type=" + entityName() + " in DB", 2, getRepository().count());
        E e1 = entities.get(0);
        E e2 = entities.get(1);
        getRepository().deleteAll(List.of(e1, e2));
        assertEquals("After deleting e1 there should be 0 entities left of type=" + entityName(), 0, getRepository().count());
    }

    @Test
    public void testDeleteAll() {
        persistEntity1And2();
        getRepository().deleteAll();
        assertEquals("There should be 0 entities of type=" + entityName() + " in DB after deleteAll()", 0, getRepository().count());
    }

}
