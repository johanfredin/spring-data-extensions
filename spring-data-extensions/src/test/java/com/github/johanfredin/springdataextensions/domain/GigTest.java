package com.github.johanfredin.springdataextensions.domain;

import com.github.johanfredin.springdataextensions.TestFixture;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class GigTest extends EntityTest<Gig> {

    @Override
    public void testDefaultConstructor() {
        Gig gig = getEmptyInstance();
        assertEquals("Id should be 0", 0L, gig.getId());
        assertNull("Date should be empty", gig.getDate());
        assertNull("Venue should be null", gig.getVenue());
        assertNull("Artist should be null", gig.getArtist());
        assertNull("Schedule should be null", gig.getSchedule());
        assertNull("Specifics should be null", gig.getSpecifics());

    }

    @Override
    public void testConstructorWithParameters() {
        Gig gig = getPopulatedInstance();
        assertEquals("Date for gig should be" + TestFixture.FAKE_DATE, TestFixture.FAKE_DATE, gig.getDate());
        assertNotNull("Venue should exist for the gig", gig.getVenue());
        assertNotNull("Gig should have Artist", gig.getArtist());
        assertEquals("Gig should have artist with name Taikes", "Taikes", gig.getArtist().getName());
        assertNotNull("Gig should have schedule", gig.getSchedule());
        assertNotNull("Gig should have specifics", gig.getSpecifics());
        assertEquals("Time for show should be 20:00", "20:00", gig.getSchedule().getTimeForShow());
        assertEquals("Time for show should be 18:00", "18:00", gig.getSchedule().getTimeForSoundcheck());
    }

    @Override
    public void testRelations() {

        GigSchedule schedule = TestFixture.getValidSchedule();
        schedule.setGig(null);

        GigSpecifics specifics = TestFixture.getValidSpecifications();
        specifics.setGig(null);

        Artist artist = TestFixture.getValidArtist();
        artist.setGigs(null);

        Venue venue = TestFixture.getValidVenue("The Whiskey");

        Gig gig = getPopulatedInstance();
        gig.setArtist(artist);
        gig.setSchedule(schedule);
        gig.setSpecifics(specifics);
        gig.setVenue(venue);

        gig.setRelations();

        assertNotNull("schedule should now have a gig", schedule.getGig());
        assertTrue("schedules gig should match this gig", schedule.getGig().equals(gig));

        assertNotNull("specifications should now have a gig", specifics.getGig());
        assertTrue("specifications gig should match this gig", specifics.getGig().equals(gig));

        assertNotNull("Artist should now have a gig", specifics.getGig());
        assertTrue("Artist gig should match this gig", specifics.getGig().equals(gig));

        /*
         * Venue has no Gig instance so instead the venues setCrossRelations method is called
         * which in turn will set the relations with address, contacts etc
         */

    }

    @Override
    public void testCopyData() {
        Gig emptyGig = getEmptyInstance();
        Gig populatedGig = getPopulatedInstance();
        populatedGig.setId(TestFixture.FAKE_ID);

        emptyGig.copyDataFromEntity(populatedGig);

        assertEquals("Empty gig Id should now be " + TestFixture.FAKE_ID, TestFixture.FAKE_ID, emptyGig.getId());
        assertEquals("Empty gig date should now be", TestFixture.FAKE_DATE, emptyGig.getDate());
        assertEquals("Empty gig specifics should now be", populatedGig.getSpecifics(), emptyGig.getSpecifics());
        assertEquals("Empty gig schedule should now be", populatedGig.getSchedule(), emptyGig.getSchedule());
        assertEquals("Empty gig venue should now be", populatedGig.getVenue(), emptyGig.getVenue());
        assertEquals("Empty gig artist should now be", populatedGig.getArtist(), emptyGig.getArtist());
    }

    @Override
    protected Gig getEmptyInstance() {
        return new Gig();
    }

    @Override
    protected Gig getPopulatedInstance() {
        return TestFixture.getValidGig();
    }


}
