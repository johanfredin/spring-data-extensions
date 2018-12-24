package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.domain.*;
import com.pocstage.venuehub.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest
@TestPropertySource(locations = "classpath:application_test.properties")
public class JpaVenueRepositoryIntegrationTest extends EmailHolderRepositoryTest<Venue, VenueRepository> {

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private VenueRepository venueRepo;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private VenueReviewRepository venueReviewRepo;

    @Autowired
    private ContactPersonRepository contactRepo;

    @Autowired
    private AddressRepository addressRepo;

    @Test
    public void testGetNameMatch() {
        super.save(getEntity1(), getEntity2());
        List<Venue> result = getRepository().findAllByNameContains("The");
        assertEquals("Finding a venue with the name starting with The should result in 1 match", 1, result.size());
    }

    @Test
    public void testGetNamesLimitedMatch() {
        final String MATCH = "The";

        Venue venue1 = getEntity1();
        Venue venue2 = getEntity2();

        venue1.setName("The Venue");
        venue2.setName("The Other Venue");

        super.save(venue1, venue2);
        List<Venue> limitedResult = getRepository().findAllByNameContains(MATCH, new PageRequest(0, 1)).getContent();
        assertEquals("Finding a venue with the name starting with The with a limited result to 1 should result in 1 match", 1, limitedResult.size());
        List<Venue> standardResult = getRepository().findAllByNameContains(MATCH);
        assertEquals("Finding a venue with the name starting with The with a standard result should result in 2 matches", 2, standardResult.size());
    }

    @Test
    public void testGettingAllVenueNames() {
        super.save(getEntity1(), getEntity2());
        List<String> allVenueNames = getRepository().getAllVenueNames();
        assertEquals("Amount of names should now be 2", 2, allVenueNames.size());
        assertEquals("Name 1 should be \"The Whiskey\"", "The Whiskey", allVenueNames.get(0));
        assertEquals("Name 2 should be \"Sticky Fingers\"", "Sticky Fingers", allVenueNames.get(1));
    }

    @Test
    public void testGettingAllVenueNamesWithLimit() {
        super.save(getEntity1(), getEntity2());
        List<String> limitedVenueNames = getRepository().getAllVenueNames(new PageRequest(0, 1)).getContent();
        assertEquals("Altough we have 2 venues persisted retrieved names should be only 1", 1, limitedVenueNames.size());
    }

    @Override
    protected Venue getEntity1() {
        Venue venue = new Venue("The Whiskey", "info@whiskey.com", "http://www.thewhiskey.com", "0763112927", 100, 1L, Genre.GRUNGE_ROCK);
        venue.setContacts(TestFixture.getValidContactsWithoutReferences());
        venue.setReviews(TestFixture.getValidReviewsWithoutReferences());
        venue.setAddress(TestFixture.getValidAddress());
        venue.setRelations();

        List<Artist> validArtists = TestFixture.getValidArtists();
        validArtists.get(0).setVenues(new ArrayList<Venue>());
        validArtists.get(0).addVenue(venue);
        validArtists.get(1).setVenues(new ArrayList<Venue>());
        validArtists.get(1).addVenue(venue);

        venue.setArtists(validArtists);

        return venue;
    }

    @Override
    protected Venue getEntity2() {
        Venue venue = new Venue("Sticky Fingers", "info@sticky.com", "http://www.stickyfingers.com", "0761253033", 1000, 2L, Genre.GRUNGE_ROCK);
        venue.setContacts(TestFixture.getValidContactsWithoutReferences());
        venue.setReviews(TestFixture.getValidReviewsWithoutReferences());
        venue.setAddress(TestFixture.getValidAddress());
        venue.setRelations();

        List<Artist> validArtists = TestFixture.getValidArtists();
        validArtists.get(0).setVenues(new ArrayList<Venue>());
        validArtists.get(0).addVenue(venue);
        validArtists.get(1).setVenues(new ArrayList<Venue>());
        validArtists.get(1).addVenue(venue);

        venue.setArtists(validArtists);

        return venue;
    }

    @Test
    @Override
    public void testCascadePersist() {
        // Acquire Venue with references
        Venue venue = getEntity1();

        // First make sure all children of venue are unpersisted
        for (int key = 0; key < TestFixture.LIST_SIZE; key++) {
            VenueReview review = venue.getReviews().get(key);
            ContactPerson contact = venue.getContacts().get(key);
            Artist artist = venue.getArtists().get(key);
            assertTrue("Review belonging to venue with id=" + venue.getId() + " should have id = 0", review.getId() < 1);
            assertTrue("Contact belonging to venue with id=" + venue.getId() + " should have id = 0", contact.getId() < 1);
            assertTrue("Artists belonging to venue with id=" + venue.getId() + " should have id = 0", artist.getId() < 1);
        }
        assertTrue("Address belonging to venue with id=" + venue.getId() + " should have id < 1", venue.getAddress().getId() < 1);

        // Now persist the venue
        long id = getRepository().save(venue).getId();

        // Make sure the venue now has valid id
        assertTrue("The venue should now have an id > 0", venue.getId() > 0);

        // Check the persisted venue
        Venue persistedVenue = getRepository().getOne(id);

        // Now make sure that the venues children have a generated id's
        for (int key = 0; key < TestFixture.LIST_SIZE; key++) {
            VenueReview review = persistedVenue.getReviews().get(key);
            ContactPerson contact = persistedVenue.getContacts().get(key);
            Artist artist = persistedVenue.getArtists().get(key);
            assertTrue("Review belonging to persisted venue with id=" + persistedVenue.getId() + " should have id > 0", review.getId() > 0);
            assertTrue("Contact belonging to persisted venue with id=" + persistedVenue.getId() + " should have id > 0", contact.getId() > 0);

            // Artists are not and should never be created when creating a venue!
            assertTrue("Artists belonging to persisted venue with id=" + persistedVenue.getId() + " should still have id < 1", artist.getId() < 1);
        }
        assertTrue("Address belonging to persisted venue with id=" + persistedVenue.getId() + " should have id > 0", persistedVenue.getAddress().getId() > 0);

        // Finally, using the child repositories. Check that fetching entities from the db dont result in null
        assertEquals("There should be 2 contacts in db now", 2, contactRepo.count());
        assertEquals("There should be 2 reviews in db now", 2, venueReviewRepo.count());
        assertTrue("There should be 0 artists in db", artistRepo.findAll().isEmpty());
        assertEquals("There should be 1 address in db now", 1, addressRepo.count());
    }

    @Test
    @Override
    public void testCascadeMerge() {
        // Fetch a persisted venue
        Venue venue = super.getPersistedEntity1();

        Address address = venue.getAddress();
        Artist artist = venue.getArtists().get(0);
        ContactPerson contactPerson = venue.getContacts().get(0);
        VenueReview venueReview = venue.getReviews().get(0);

        // Test the things we want to change before the change
        assertEquals("Current venue name should be The Whiskey", "The Whiskey", venue.getName());
        assertEquals("Current address street should be Street", "Street", address.getStreet());
        assertEquals("Current artist name should be Taikes", "Taikes", artist.getName());
        assertEquals("Current contact name should be Joe", "Joe", contactPerson.getName());
        assertEquals("Current review text should be Taikes thinks this place is awesome", "Taikes thinks this place is awesome", venueReview.getReview());

        // Now start changing the properties
        venue.setName("Castellos");
        address.setStreet("Hagåkersgatan");
        artist.setName("Oasis");
        contactPerson.setName("Jennifer");
        venueReview.setReview("This place stinks!");
        venueReview.setScore((byte) 1);

        // Persist the artist separately since cascade persist dont exist for artist
        // We have to do this in order for a cascade merge to take effect for artist
        this.artistRepo.save(artist);

        // Now update the venue
        getRepository().save(venue);

        // Basically pointless but just to make sure we get a fresh copy from the db after merge
        Venue dbVenue = getRepository().getOne(venue.getId());

        // Fetch persisted children from db, will not be needed in web controllers but here we do it to make sure it took effect
        Address dbAddress = this.addressRepo.getOne(address.getId());
        Artist dbArtist = this.artistRepo.getOne(artist.getId());
        ContactPerson dbContact = this.contactRepo.getOne(contactPerson.getId());
        VenueReview dbReview = this.venueReviewRepo.getOne(venueReview.getId());

        // assert updated values were persisted
        assertEquals("Venue name should now be Castellos", "Castellos", dbVenue.getName());
        assertEquals("Address street should now be Hagåkersgatan", "Hagåkersgatan", dbAddress.getStreet());
        assertEquals("Artist name should now be Oasis", "Oasis", dbArtist.getName());
        assertEquals("Contact name should now be Jennifer", "Jennifer", dbContact.getName());
        assertEquals("Review text should now be This place stinks!", "This place stinks!", dbReview.getReview());
    }

    @Test
    @Override
    public void testCascadeDelete() {
        // Fetch a persisted venue
        Venue venue = super.getPersistedEntity1();

        // Must persist the Artist since not persisted in a cascade
        this.artistRepo.save(venue.getArtists().get(0));

        // Fetch persisted children from db, will not be needed in web controllers but here we do it to make sure it took effect
        Address dbAddress = this.addressRepo.getOne(venue.getAddress().getId());
        Artist dbArtist = this.artistRepo.getOne(venue.getArtists().get(0).getId());
        ContactPerson dbContact = this.contactRepo.getOne(venue.getContacts().get(0).getId());
        VenueReview dbReview = this.venueReviewRepo.getOne(venue.getReviews().get(0).getId());

        // Now delete the venue
        getRepository().delete(venue);

        // Now make sure that the children with cascade delete are removed as well
        assertNull("There should be no address with id=" + dbAddress.getId() + " in dbAnymore", this.addressRepo.getOne(dbAddress.getId()));
        assertNull("There should be no contact person with id=" + dbContact.getId() + " in dbAnymore", this.contactRepo.getOne(dbContact.getId()));
        assertNull("There should be no review with id=" + dbReview.getId() + " in dbAnymore", this.venueReviewRepo.getOne(dbReview.getId()));

        assertNotNull("The artist with id=" + dbArtist.getId() + " should still exist in db", this.artistRepo.getOne(dbArtist.getId()));
    }

    @Test
    public void testGetEntityWithChildren() {
        long id = getFullyPopulatedPersistedEntity(true).getId();
        Venue venue = getRepository().getVenueWithChildren(id, true, true, true, true);

        for (int i = 0; i < TestFixture.LIST_SIZE; i++) {
            assertTrue("Artist nr " + i + "'s venue should be our venue", venue.getArtists().get(i).getVenues().contains(venue));
            assertTrue("Contact nr " + i + "'s venue should be our venue", venue.getContacts().get(i).getVenue().equals(venue));
            assertTrue("Review nr " + i + "'s venue should be our venue", venue.getReviews().get(i).getVenue().equals(venue));
        }
        assertTrue("address's venue should be our venue", venue.getAddress().getVenue().equals(venue));
    }

    @Test
    public void testGetVenuesForMember() {
        // Fetch the 3 entities required
        List<Member> members = TestFixture.getValidMembersWithoutReferences();
        List<Artist> artists = TestFixture.getValidArtists();
        List<Venue> venues = TestFixture.getValidVenuesWithoutReferences();

        Venue anotherVenue = TestFixture.getValidVenueWithoutReferences("Craigs Basement");
        List<Venue> anotherListOfVenues = new ArrayList<Venue>();
        anotherListOfVenues.add(anotherVenue);

        // Set the relations
        Member member = members.get(0);
        Artist artist1 = artists.get(0);
        Artist artist2 = artists.get(1);

        artist1.setVenues(venues);
        artist2.setVenues(anotherListOfVenues);
        member.setArtists(artists);

        getRepository().saveAll(venues);
        getRepository().saveAll(anotherListOfVenues);
        this.artistRepo.saveAll(artists);
        this.memberRepo.save(member);

        List<Venue> venuesForMember = getRepository().getVenuesForMember(member.getId());
        assertEquals("Venues related to the artist of the member should be 3", 3, venuesForMember.size());
    }

    @Override
    protected VenueRepository getRepository() {
        return venueRepo;
    }

    @Override
    protected Venue getFullyPopulatedUnpersistedEntity(boolean biDirectional) {
        return getEntity1();
    }

    @Override
    protected Venue getFullyPopulatedPersistedEntity(boolean biDirectional) {
        Venue venue = getFullyPopulatedUnpersistedEntity(biDirectional);
        for (Artist artist : venue.getArtists()) {
            this.artistRepo.save(artist);
        }
        return getRepository().save(venue);
    }


}
