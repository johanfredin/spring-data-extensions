package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.domain.Identifiable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


/**
 * An abstract test class that tests basic CRUD functionality. All repositories
 * can reuse these tests, hereby reducing code duplication.
 * <p>
 * This class uses the Template pattern. This abstract class is the template
 * that coordinates basic test cases. The concrete implementations provide the
 * specific details, such as which entities and repositories to use etc.
 * <p>
 * Also includes abstract methods for testing cascading operations for PERSIST, MERGE and DELETE.
 * Subclasses using cascade operations should implement the ones used
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest
public abstract class AbstractJpaRepositoryTest<ID extends Serializable, E extends Identifiable<ID>, R extends BaseRepository<ID, E>> {

    protected Logger log = LogManager.getLogger(this.getClass());

    @Test
    public void testGettingPersistedEntity1And2() {
        // Just check that no exception is thrown
        persistEntities1And2();
    }

    @Test
    public void testPersist() {
        E e1 = getRepository().save(getEntity1());
        E e2 = getRepository().save(getEntity2());

        assertTrue("Entity 1 ID", e1.isExistingEntity());
        assertTrue("Entity 2 ID", e2.isExistingEntity());
        log.info("testCreate() was called");
    }

    @Test
    public void testRemove() {
        E entity = getRepository().getOne(getRepository().save(getEntity1()).getId());
        assertNotNull("Entity is not null", entity);

        getRepository().delete(entity);
        assertNull("Deleted object return null", getRepository().getOne(entity.getId()));
        log.info("testRemove() was called");
    }

    @Test
    public void testFindById() {
        long id = getRepository().save(getEntity1()).getId();
        E entity = getRepository().getOne(id);
        assertNotNull("Entity is not null", entity);
        assertEquals("Id is correct", id, entity.getId());
    }

    @Test
    public void testUpdate() {
        E entity = getRepository().save(getEntity1());

        entity.setLastChangeDate("2015-09-25:18:00:25");

        // This doesn´t test much, but is still some control that no exception
        // is thrown.
        getRepository().save(entity);
    }

    @Test
    public void testGetAllEntities() {
        persistEntities1And2();
        assertEquals("Persisted entities in db should be 2", 2, getRepository().count());
    }

    @Test
    public void testGetAllEntitiesWithLimit() {
        persistEntities1And2();
        assertEquals("Calling get all entities with a limit to 1 should result in 1 match when there are actually 2", 1, getRepository().findAll(new PageRequest(0, 1)).getSize());
        assertEquals("All persisted entities should be 2", 2, getRepository().count());
    }

    @Test
    public void testGetSortedEntities() {
        E e1 = getPersistedEntity1();
        E e2 = getPersistedEntity2();

        String date1 = "2015-09-25 08:46:10";
        String date2 = "2016-09-25 08:46:10";

        // Update creation date, this should never get updated in production but for the intent we use it here
        e1.setCreationDate(date1);
        e2.setCreationDate(date2);

        // Update the entities in db
        save(e1, e2);

        // Try the ascending order
        List<E> sortedEntitiesAsc = getRepository().findAll(new Sort(Direction.ASC, "creationDate"));
        assertEquals("First entry's date in ascending order should be " + date1, date1, sortedEntitiesAsc.get(0).getCreationDate());

        // Try the descending order
        List<E> sortedEntitiesDesc = getRepository().findAll(new Sort(Direction.DESC, "creationDate"));
        assertEquals("First entry's date in descending order should be " + date2, date2, sortedEntitiesDesc.get(0).getCreationDate());
    }

    @Test
    public void testGetSortedEntitiesWithLimit() {
        E e1 = getPersistedEntity1();
        E e2 = getPersistedEntity2();

        String date1 = "2015-09-25 08:46:10";
        String date2 = "2016-09-25 08:46:10";

        // Update creation date, this should never get updated in production but for the intent we use it here
        e1.setCreationDate(date1);
        e2.setCreationDate(date2);

        // Update the entities in db
        save(e1, e2);

        // Try the ascending order
        Page<E> sortedEntitiesAsc = getRepository().findAll(PageRequest.of(0, 1, new Sort(Direction.ASC, "creationDate")));
        assertEquals("First entry's date, ascending order should be " + date1, date1, sortedEntitiesAsc.getContent().get(0).getCreationDate());
        assertEquals("List size should be 1", 1, sortedEntitiesAsc.getSize());
    }

    // Regular methods -----------------------------------------------------------------------

    /**
     * @return a persisted entity with properties from {@link #getEntity1()}
     */
    protected E getPersistedEntity1() {
        return getRepository().save(getEntity1());
    }

    /**
     * @return a persisted entity with properties from {@link #getEntity2()}
     */
    protected E getPersistedEntity2() {
        return getRepository().save(getEntity2());
    }

    /**
     * Persists the 2 entities calling
     * {@link #getEntity1()}
     * and #getEn
     */
    protected List<E> persistEntities1And2() {
        return save(getEntity1(), getEntity2());
    }

    @SafeVarargs
    protected final List<E> save(E... entities) {
        List<E> collection = new ArrayList<>();
        for (E entity : entities) {
            collection.add(entity);
        }
        return getRepository().saveAll(collection);
    }


    // Abstracts ---------------------------------------------------------------------------------------

    /**
     * Subclasses must return the repository to use
     */
    protected abstract R getRepository();

    /**
     * Sublcasses must return a valid {@link Identifiable} to save.
     */
    protected abstract E getEntity1();

    /**
     * Sublclasses must return a second valid {@link Identifiable}. Should be different from
     * {@link #getEntity1()}.
     */
    protected abstract E getEntity2();

    /**
     * Creates a new entity E and populates all its children and relations.
     * The main entity and its children will not be persisted in the DB.
     *
     * @param biDirectional whether or not to have the children set their relations with their parent E as well
     * @return a fully populated entity E with all its children unpersisted in DB
     */
    protected abstract E getFullyPopulatedUnpersistedEntity(boolean biDirectional);

    /**
     * Creates a new entity E and populates all its children and relations.
     * The main entity and its children willbe persisted in the DB.
     *
     * @param biDirectional whether or not to have the children set their relations with their parent E as well
     * @return a fully populated entity E with all its children persisted in DB
     */
    protected abstract E getFullyPopulatedPersistedEntity(boolean biDirectional);


}
