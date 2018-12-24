package com.github.johanfredin.springdataextensions.domain;

import com.github.johanfredin.springdataextensions.TestFixture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class VenueTest extends EntityTest<Venue> {

    @Override
    public void testDefaultConstructor() {
        Venue venue = getEmptyInstance();
        assertEquals("Id should be 0", 0L, venue.getId());

        assertNull("Venue should have no name yet", venue.getName());
        assertNull("Venue should have no email yet", venue.getEmail());
        assertNull("Venue should have no url yet", venue.getUrl());
        assertNull("Venue should have no phone nr yet", venue.getPhoneNr());
        assertNull("Venue should have no genre yet", venue.getGenre());
        assertEquals("Venue capacity should be 0", 0, venue.getCapacity());
        assertEquals("Last change user Id should be 0", 0L, venue.getLastChangedByUsedId());

        assertNull("Venue should have no reviews yet", venue.getReviews());
        assertNull("Venue should have no contact person yet", venue.getContacts());
        assertNull("Venue should have no adress yet", venue.getAddress());
        assertNull("Venue should have no artists yet", venue.getArtists());
    }

    @Override
    public void testConstructorWithParameters() {
        Venue venue = getPopulatedInstance();

        assertEquals("Venue name should be", "The Whiskey", venue.getName());
        assertEquals("Venue email should be", "info@thewhiskey.com", venue.getEmail());
        assertEquals("Venue url should be", "http://www.thewhiskey.com", venue.getUrl());
        assertEquals("Venue phone nr should be " + TestFixture.FAKE_PHONE, TestFixture.FAKE_PHONE, venue.getPhoneNr());
        assertEquals("Venue capacity should be 100", 100, venue.getCapacity());
        assertEquals("Venue last change user id should be 1", 1L, venue.getLastChangedByUsedId());
        assertEquals("Genre of venue should be " + TestFixture.FAKE_GENRE, TestFixture.FAKE_GENRE, venue.getGenre());

        assertNotNull("Venue should have reviews", venue.getReviews());
        assertNotNull("Venue should have contacts", venue.getContacts());
        assertNotNull("Venue should have adress", venue.getAddress());
        assertNotNull("Venue should have artists", venue.getArtists());
    }

    @Test
    public void testAddingContactPerson() {
        Venue venue = getPopulatedInstance();
        venue.setContacts(new ArrayList<ContactPerson>());
        venue.addContact(TestFixture.getValidContactPerson("Joe"));
        assertEquals("Venue should have 1 contact person", 1, venue.getContacts().size());
        venue.addContact(TestFixture.getValidContactPerson("Jane"));
        assertEquals("Venue should now have 2 contactpersons", 2, venue.getContacts().size());
    }

    @Test
    public void testRemovingContactPerson() {
        Venue venue = getPopulatedInstance();
        venue.setContacts(null);

        venue.setContacts(TestFixture.getValidContacts());
        assertEquals("Venue should now have 2 contact persons", 2, venue.getContacts().size());

        ContactPerson joe = venue.getContacts().iterator().next();
        venue.removeContact(joe);
        assertEquals("Venue should now have 1 contact person", 1, venue.getContacts().size());
        assertEquals("First Name of remaining contact person should be Jane", "Jane", venue.getContacts().iterator().next().getName());
    }

    @Test
    public void testPhoneAndEmail() {
        Venue venue = getPopulatedInstance();
        venue.setPhoneNr("+46 761 25 30 33");
        venue.setEmail("yolo@yolo.com");
        assertEquals("Phone nr should be \"+46 761 25 30 33\"", "+46 761 25 30 33", venue.getPhoneNr());
        assertEquals("email should be \"yolo@yolo.com\"", "yolo@yolo.com", venue.getEmail());
    }

    @Override
    public void testRelations() {
        Venue venue = getPopulatedInstance();
        venue.setAddress(null);
        venue.setContacts(null);
        venue.setReviews(null);

        Address address = TestFixture.getValidAddress();
        address.setVenue(null);

        List<ContactPerson> validContacts = TestFixture.getValidContacts();
        for (ContactPerson contact : validContacts) {
            contact.setVenue(null);
        }

        List<VenueReview> validReviews = TestFixture.getValidReviews();
        for (VenueReview review : validReviews) {
            review.setVenue(null);
        }

        venue.setAddress(address);
        venue.setContacts(validContacts);
        venue.setReviews(validReviews);

        // Artists wont get set in setCrossRelations() but just to check that that is not the case we give it some
        List<Artist> validArtists = TestFixture.getValidArtists();
        for (Artist artist : validArtists) {
            artist.setVenues(null);
        }
        venue.setArtists(validArtists);


        venue.setRelations();

        assertNotNull("Address should have a venue now", address.getVenue());
        assertTrue("Address venue should be the same as venue", address.getVenue().equals(venue));

        for (ContactPerson person : venue.getContacts()) {
            assertNotNull("Contact person should have a venue now", person.getVenue());
            assertTrue("Contacts venue should be the same as this venue", person.getVenue().equals(venue));
        }

        for (VenueReview review : venue.getReviews()) {
            assertNotNull("Review person should have a venue now", review.getVenue());
            assertTrue("Contacts venue should be the same as this venue", review.getVenue().equals(venue));
        }

        for (Artist artist : validArtists) {
            assertNull("Venues should still be null", artist.getVenues());
        }
    }

    @Override
    public void testCopyData() {
        Venue emptyVenue = getEmptyInstance();

        Venue populatedVenue = getPopulatedInstance();
        populatedVenue.setId(TestFixture.FAKE_ID);

        emptyVenue.copyDataFromEntity(populatedVenue);

        assertEquals("Empty venue name should now be " + TestFixture.FAKE_VENUE_NAME, TestFixture.FAKE_VENUE_NAME, emptyVenue.getName());
        assertEquals("Empty venue email should now be " + TestFixture.getFakeMail(TestFixture.FAKE_VENUE_NAME), TestFixture.getFakeMail(TestFixture.FAKE_VENUE_NAME), emptyVenue.getEmail());
        assertEquals("Empty venue url should now be", "http://www.thewhiskey.com", emptyVenue.getUrl());
        assertEquals("Empty venue phone nr should now be " + TestFixture.FAKE_PHONE, TestFixture.FAKE_PHONE, emptyVenue.getPhoneNr());
        assertEquals("Empty venue capacity should now be " + TestFixture.FAKE_CAPACITY, TestFixture.FAKE_CAPACITY, emptyVenue.getCapacity());
        assertEquals("Empty venue last change user id should now be 1", 1L, emptyVenue.getLastChangedByUsedId());
        assertNotNull("Empty venue last change date should not be null", emptyVenue.getLastChangeDate());
        assertEquals("Genre of venue should be " + TestFixture.FAKE_GENRE, TestFixture.FAKE_GENRE, emptyVenue.getGenre());

        assertNotNull("Empty venue should now have reviews", emptyVenue.getReviews());
        assertNotNull("Empty venue should now have contacts", emptyVenue.getContacts());
        assertNotNull("Empty venue should now have adress", emptyVenue.getAddress());
        assertNotNull("Empty venue should now have artists", emptyVenue.getArtists());
    }

    @Override
    protected Venue getEmptyInstance() {
        return new Venue();
    }

    @Override
    protected Venue getPopulatedInstance() {
        return TestFixture.getValidVenue();
    }

}
