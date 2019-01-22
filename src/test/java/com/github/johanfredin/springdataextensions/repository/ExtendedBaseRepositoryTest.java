package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.domain.Identifiable;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Tests all the methods in {@link ExtendedBaseRepository}
 *
 * @param <ID> any {@link Object} that is used as the primary id for the {@link Identifiable} type this service is working with
 * @param <T>  Any JPA entity extending {@link Identifiable}
 * @param <R>  Any class extending {@link BaseRepository}
 * @author johan
 */
public abstract class ExtendedBaseRepositoryTest<ID, T extends Identifiable<ID>, R extends ExtendedBaseRepository<ID, T>>
        extends BaseRepositoryIntegrationTest<ID, T, R> {

    /**
     * Test {@link ExtendedBaseRepository#saveAll(Identifiable[])}
     */
    @Test
    public void testSaveAllListArbiraryArguments() {
        var listWithArbitraryArguments = getRepository().saveAll(getEntity1(), getEntity2());
        assertEquals("List", true, listWithArbitraryArguments instanceof List);
        assertPersisted(listWithArbitraryArguments);
    }

    /**
     * Test {@link ExtendedBaseRepository#saveAllAsSet(Identifiable[])}
     */
    @Test
    public void testSaveAllAsSet() {
        var setWithArbitraryArguments = getRepository().saveAllAsSet(getEntity1(), getEntity2());
        assertEquals("Set", true, setWithArbitraryArguments instanceof Set);
        assertPersisted(setWithArbitraryArguments);
    }

    /**
     * Test {@link ExtendedBaseRepository#saveAll(List)}
     */
    @Test
    public void testSaveAllList() {
        var list = getRepository().saveAll(List.of(getEntity1(), getEntity2()));
        assertEquals("List", true, list instanceof List);
        assertPersisted(list);
    }

    /**
     * Test {@link ExtendedBaseRepository#saveAll(Set)}
     */
    @Test
    public void testSaveAllSet() {
        var set = getRepository().saveAll(Set.of(getEntity1(), getEntity2()));
        assertEquals("Set", true, set instanceof Set);
        assertPersisted(set);
    }

    /**
     * Test {@link ExtendedBaseRepository#deleteAll(Identifiable[])}
     */
    @Test
    public void testDeleteAllCollectionArbitraryAmount() {
        var set = getRepository().saveAllAsSet(getEntity1(), getEntity2());
        getRepository().deleteAll(set);
        assertDeleted(set);
    }

    /**
     * Test {@link ExtendedBaseRepository#deleteAll(Identifiable[])}
     */
    @Test
    public void testDeleteAllCollection() {
        var set = getRepository().saveAll(Set.of(getEntity1(), getEntity2()));
        getRepository().deleteAll(set);
        assertDeleted(set);
    }

}