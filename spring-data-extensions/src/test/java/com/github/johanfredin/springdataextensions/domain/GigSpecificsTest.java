package com.github.johanfredin.springdataextensions.domain;

import com.github.johanfredin.springdataextensions.TestFixture;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class GigSpecificsTest extends EntityTest<GigSpecifics> {

    @Override
    public void testDefaultConstructor() {
        GigSpecifics specifics = getEmptyInstance();
        assertEquals("Id should be 0", 0L, specifics.getId());
        assertEquals("Revenue should be 0", 0, specifics.getRevenue());
        assertFalse("Has backline should be false", specifics.isBackline());
        assertFalse("Travel compensation should be false", specifics.isTravelCompensation());
        assertFalse("Food included should be false", specifics.isFoodIncluded());
        assertFalse("Free beets should be false", specifics.isFreeBeverages());
        assertFalse("Has backstage should be false", specifics.isBackstage());
        assertNull("Notes should be empty", specifics.getSideNotes());
        assertNull("Specifications should not have a gig", specifics.getGig());
    }

    @Override
    public void testConstructorWithParameters() {
        GigSpecifics specifications = getPopulatedInstance();
        assertEquals("Revenue should be 1000", 1000, specifications.getRevenue());
        assertTrue("Has backline should be true", specifications.isBackline());
        assertFalse("Travel compensation should be false", specifications.isTravelCompensation());
        assertTrue("Food included should be false", specifications.isFoodIncluded());
        assertTrue("Free beets should be false", specifications.isFreeBeverages());
        assertTrue("Has backstage should be false", specifications.isBackstage());
        assertEquals("Notes should be \"Bring extra underwear\"", "Bring extra underwear", specifications.getSideNotes());
        assertNotNull("Specifications should belong to a gig", specifications.getGig());
    }

    @Override
    public void testRelations() {
        Gig gig = TestFixture.getValidGigWithoutReferences();
        gig.setSpecifics(null);

        GigSpecifics specifics = getEmptyInstance();
        specifics.setGig(gig);

        specifics.setRelations();

        assertNotNull("Gig should now have specifics relations", gig.getSpecifics());
        assertTrue("Gig should have the same specifics as specifics that sat the realations", specifics.getGig().equals(gig));
    }

    @Override
    public void testCopyData() {
        GigSpecifics emptySpecifics = getEmptyInstance();
        GigSpecifics populatedSpecifics = TestFixture.getValidSpecifications();
        populatedSpecifics.setId(TestFixture.FAKE_ID);

        emptySpecifics.copyDataFromEntity(populatedSpecifics);
        assertEquals("Empty gig Id should be " + TestFixture.FAKE_ID, TestFixture.FAKE_ID, emptySpecifics.getId());
        assertEquals("Revenue should be 1000", 1000, emptySpecifics.getRevenue());
        assertTrue("Has backline should be true", emptySpecifics.isBackline());
        assertFalse("Travel compensation should be false", emptySpecifics.isTravelCompensation());
        assertTrue("Food included should be false", emptySpecifics.isFoodIncluded());
        assertTrue("Free beets should be false", emptySpecifics.isFreeBeverages());
        assertTrue("Has backstage should be false", emptySpecifics.isBackstage());
        assertEquals("Notes should be \"Bring extra underwear\"", "Bring extra underwear", emptySpecifics.getSideNotes());
        assertNotNull("Specifications should belong to a gig", emptySpecifics.getGig());
    }

    @Override
    protected GigSpecifics getEmptyInstance() {
        return new GigSpecifics();
    }

    @Override
    protected GigSpecifics getPopulatedInstance() {
        return TestFixture.getValidSpecifications();
    }

}
