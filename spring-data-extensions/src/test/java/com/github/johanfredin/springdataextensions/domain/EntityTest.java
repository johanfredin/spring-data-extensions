package com.github.johanfredin.springdataextensions.domain;

import com.github.johanfredin.springdataextensions.TestFixture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Helper test superclass for the 3 most common test methods
 *
 * @author johan
 */
@RunWith(JUnit4.class)
public abstract class EntityTest<E extends AbstractEntity> {

    /**
     * {@link AbstractEntity#getCreationDate()} every time new instance is created, makes sure!
     */
    @Test
    public void testCreationDateSet() {
        E entity = getEmptyInstance();
        assertNotNull("creationDate should not be null!", entity.getCreationDate());
    }

    /**
     * Make sure that {@link AbstractEntity#updateLastChangeDate()} is called when calling each subentities copyDataMethod
     */
    @Test
    public void testLastChangeDateCalledWhenDataCopied() {
        E emptyEntity = getEmptyInstance();
        assertNotNull("lastChangeDate should not be null", emptyEntity.getLastChangeDate());

        emptyEntity.setLastChangeDate(TestFixture.FAKE_LAST_UPDATE);
        E populatedEntity = getPopulatedInstance();
        emptyEntity.copyDataFromEntity(populatedEntity);

        assertTrue("last change date should no longer be equal to:" + TestFixture.FAKE_LAST_UPDATE, !emptyEntity.getLastChangeDate().equals(TestFixture.FAKE_LAST_UPDATE));
    }

    /**
     * Use this when you want to test the default constructor without params
     */
    @Test
    public abstract void testDefaultConstructor();

    /**
     * Use this when you want to test the constructor with params
     */
    @Test
    public abstract void testConstructorWithParameters();

    /**
     * Use for testing backward relations see {@link AbstractEntity#setRelations()}
     */
    @Test
    public abstract void testRelations();

    /**
     * Use for testing the method {@link AbstractEntity#copyDataFromEntity(com.pylonmusic.gigmanager.domain.IdHolder)}
     */
    @Test
    public abstract void testCopyData();

    /**
     * @return a new instance of {@link AbstractEntity} with no properties initiated (e.g new Instance())
     */
    protected abstract E getEmptyInstance();

    /**
     * @return a new instance of {@link AbstractEntity} with properties initiated (e.g new Instance(field1, field2, obj 3))
     */
    protected abstract E getPopulatedInstance();
}
