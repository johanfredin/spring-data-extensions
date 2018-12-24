package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.domain.*;
import com.pocstage.venuehub.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest
@TestPropertySource(locations = "classpath:application_test.properties")
public class JpaGigRepositoryIntegrationTest extends AbstractJpaRepositoryTest<Gig, GigRepository> {

    @Autowired
    private GigRepository gigRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private GigSpecificsRepository gigSpecificsRepo;

    @Autowired
    private GigScheduleRepository gigScheduleRepo;

    @Autowired
    private ContactPersonRepository contactPersonRepo;

    @Test
    public void testgetAllEntitiesAndVenues() {
        save(getEntity1(), getEntity2());
        assertEquals("Amount of gigs should now be 2", 2, getRepository().count());
        assertEquals("Amount of venues should also be 2", 2, this.venueRepository.count());
    }

    @Test
    public void testGigAndGigScheduleRelation() {
        long id = getRepository().save(getEntity1()).getId();
        Gig saveedGig = getRepository().getOne(id);
        GigSchedule schedule = saveedGig.getSchedule();

        assertNotNull("schedule should not be null", schedule);
        assertNotNull("schedule should be assigned to a gig", schedule.getGig());
        assertTrue("schedules gig id should equal saveed gigs id", schedule.getGig().getId() == id);

        assertEquals("Time for show should be 20:00", "20:00", schedule.getTimeForShow());
        assertEquals("Time for soundcheck should be 18:00", "18:00", schedule.getTimeForSoundcheck());
        assertEquals("Opening time should be 19:00", "19:00", schedule.getOpeningTime());
        assertEquals("Showup time should be 14:00", "14:00", schedule.getShowupTime());
        assertEquals("Closing time should be 02:00", "02:00", schedule.getClosingTime());
    }

    @Override
    protected GigRepository getRepository() {
        return gigRepository;
    }

    @Override
    protected Gig getEntity1() {
        Venue validVenue = TestFixture.getValidVenue();
        validVenue.setAddress(TestFixture.getValidAddress());
        validVenue.setContacts(TestFixture.getValidContactsWithoutReferences());

        Artist validArtist = TestFixture.getValidArtist("Taikes", "Gothenburg", "Sweden");
        validArtist.setGigs(new ArrayList<Gig>());

        // Artist is not saveed in a cascade operation, we have to save it manually
        this.artistRepo.save(validArtist);

        Gig gig = new Gig();
        gig.setDate(TestFixture.FAKE_DATE);
        gig.setArtist(validArtist);
        gig.setVenue(validVenue);
        gig.setSchedule(TestFixture.getValidScheduleWithoutReference());
        gig.setSpecifics(TestFixture.getValidSpecificationsWithoutReference());

        gig.setRelations();
        return gig;
    }

    @Override
    protected Gig getEntity2() {
        Venue validVenue = TestFixture.getValidVenue();
        validVenue.setAddress(TestFixture.getValidAddress());
        validVenue.setContacts(TestFixture.getValidContactsWithoutReferences());

        Artist validArtist = TestFixture.getValidArtist("Oasis", "Manchester", "UK");
        validArtist.setGigs(new ArrayList<Gig>());

        // Artist is not saveed in a cascade operation, we have to save it manaually
        this.artistRepo.save(validArtist);

        Gig gig = new Gig();
        gig.setDate("2008-09-01");
        gig.setArtist(validArtist);
        gig.setVenue(validVenue);
        gig.setSchedule(TestFixture.getValidScheduleWithoutReference());
        gig.setSpecifics(TestFixture.getValidSpecificationsWithoutReference());

        gig.setRelations();
        return gig;
    }

    @Override
    public void testCascadePersist() {
        // Acquire an unsaveed gig with references
        Gig gig = getEntity1();
        long gigId = gig.getId();

        // The artist and the venue are saveed!
        Artist artist = gig.getArtist();
        Venue venue = gig.getVenue();

        long artistId = artist.getId();

        GigSpecifics specifics = gig.getSpecifics();
        GigSchedule schedule = gig.getSchedule();

        // First make sure all children with a cascade save are unsaveed
        assertTrue("Schedule assigned to gig with id=" + gigId + " should have id < 1", schedule.getId() < 1);
        assertTrue("Specifics assigned to gig with id=" + gigId + " should have id < 1", specifics.getId() < 1);
        assertTrue("Venue assigned to gig with id=" + gigId + " should have id < 1", venue.getId() < 1);

        // Now save the gig
        long savedGigId = getRepository().save(gig).getId();

        // Now make sure the child entities got saveed as well
        assertTrue("Schedule assigned to saveed gig with id=" + savedGigId + " should have id > 0", schedule.getId() > 0);
        assertTrue("Specifics assigned to saveed gig with id=" + savedGigId + " should have id > 0", specifics.getId() > 0);
        assertTrue("Venue assigned to saveed gig with id=" + savedGigId + " should have id > 0", venue.getId() > 0);

        // There should be at least 1 schedule and 1 specifics entity in db
        assertEquals("There should be at least 1 schedule in db", 1, gigScheduleRepo.count());
        assertEquals("There should be at least 1 specification in db", 1, gigSpecificsRepo.count());
        assertEquals("There should be at least 1 venue in db", 1, venueRepository.count());

        // Check that the venue and artist id's where not affected during the save
        assertTrue("Artist id should be the same as before", gig.getArtist().getId() == artistId);
    }

    @Override
    public void testCascadeMerge() {
        // Acquire a saveed gig
        Gig gig = super.getPersistedEntity1();

        // Get the children that will be effected by the merge
        Artist artist = gig.getArtist();
        Venue venue = gig.getVenue();
        GigSchedule schedule = gig.getSchedule();
        GigSpecifics specifics = gig.getSpecifics();

        // Change the childrens properties
        gig.setDate("1991-09-23");
        artist.setName("Nirvana");
        artist.setCity("Seattle");
        schedule.setTimeForSoundcheck("04:00");
        specifics.setSideNotes("The owner is kind of weird");
        venue.setName("Jasons Hole");

        // Lets try git to dig a bit deeper
        ContactPerson contact = venue.getContacts().get(0);
        // The contact is no child of the gig but it is a child of the venue OF the gig
        // updating the gig should update the venue which in turn should set update the contact.
        contact.setName("Todd");


        // Now update the gig ----------------
        getRepository().save(gig);

        // Get the updated children from db to make sure
        Artist dbArtist = artistRepo.getOne(artist.getId());
        Venue dbVenue = venueRepository.getOne(venue.getId());
        GigSchedule dbSchedule = gigScheduleRepo.getOne(schedule.getId());
        GigSpecifics dbSpecifics = gigSpecificsRepo.getOne(specifics.getId());
        ContactPerson dbContact = contactPersonRepo.getOne(contact.getId());

        // Make sure the properties were updated in the db
        assertEquals("Updated artist name should be Nirvana", "Nirvana", dbArtist.getName());
        assertEquals("Updated artist city should be Seattle", "Seattle", dbArtist.getCity());
        assertEquals("Updated schedules time for soundcheck should be 04:00", "04:00", dbSchedule.getTimeForSoundcheck());
        assertEquals("Updated specifics sidenotes should be \"The owner is kind of weird\"", "The owner is kind of weird", dbSpecifics.getSideNotes());
        assertEquals("Updated venue name should be \"Jasons Hole\"", "Jasons Hole", dbVenue.getName());
        assertEquals("Updated contact of updated venue should have name=\"Todd\"", "Todd", dbContact.getName());
    }

    @Override
    public void testCascadeDelete() {
        // Acquire saveed gig
        Gig gig = super.getPersistedEntity1();

        // Get the children that will be effected by the delete
        GigSchedule schedule = gig.getSchedule();
        GigSpecifics specifics = gig.getSpecifics();

        // Artist and Venue should not be affected, we will verify that here
        Artist artist = gig.getArtist();
        Venue venue = gig.getVenue();

        // Delete the gig
        getRepository().delete(gig);

        // Verify that schedule and specifics were deleted as well
        assertNull("There should be no longer be a GigSpecification instance with id=" + specifics.getId() + " in db", gigSpecificsRepo.getOne(specifics.getId()));
        assertNull("There should be no longer be a GigSchedule instance with id=" + schedule.getId() + " in db", gigScheduleRepo.getOne(schedule.getId()));

        // Verify that the venue and the artist still exist
        assertNotNull("Artist with id=" + artist.getId() + " should still exist in db", artistRepo.getOne(artist.getId()));
        assertNotNull("Venue with id=" + venue.getId() + " should still exist in db", venueRepository.getOne(venue.getId()));
    }

    @Test
    public void testGetEntityWithChildren() {
//		long id = getFullyPopulatedPersistedEntity(true).getId();
    }

    @Override
    protected Gig getFullyPopulatedUnpersistedEntity(boolean biDirectional) {
        Venue validVenue = TestFixture.getValidVenue();
        validVenue.setAddress(TestFixture.getValidAddress());
        validVenue.setContacts(TestFixture.getValidContactsWithoutReferences());

        Artist validArtist = TestFixture.getValidArtist("Taikes", "Gothenburg", "Sweden");

        // Artist is not saveed in a cascade operation, we have to save it manually
        this.artistRepo.save(validArtist);

        Gig gig = TestFixture.getValidGigWithoutReferences();
        gig.setArtist(validArtist);
        gig.setVenue(validVenue);
        gig.setSchedule(TestFixture.getValidScheduleWithoutReference());
        gig.setSpecifics(TestFixture.getValidSpecificationsWithoutReference());

        if (biDirectional) {
            validVenue.setRelations();
            validArtist.setRelations();
            gig.setRelations();
        }

        return gig;

    }

    @Override
    protected Gig getFullyPopulatedPersistedEntity(boolean biDirectional) {
        Gig gig = getFullyPopulatedUnpersistedEntity(biDirectional);
        getRepository().save(gig);
        this.artistRepo.save(gig.getArtist());
        return gig;
    }

}
