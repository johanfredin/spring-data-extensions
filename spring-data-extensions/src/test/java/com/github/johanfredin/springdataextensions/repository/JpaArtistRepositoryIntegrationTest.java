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
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest
@TestPropertySource(locations = "classpath:application_test.properties")
public class JpaArtistRepositoryIntegrationTest extends EmailHolderRepositoryTest<Artist, ArtistRepository> {

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private VenueRepository venueRepo;

    @Autowired
    private VenueReviewRepository venueReviewRepo;

    @Autowired
    private GigRepository gigRepository;

    @Autowired
    private GigRequestRepository gigRequestRepo;

    @Override
    public void testCascadePersist() {
        // Artist has no cascade persist operation to any of its children, we verify that here.
        Artist artist = getFullyPopulatedUnpersistedEntity(true);

        // Verify artist is not peristed
        assertTrue("No artists should exist yet", getRepository().count() <= 0);

        // Make sure there are no persisted children
        assertTrue("Venue should have id < 1", artist.getVenues().get(0).getId() < 1);
        assertTrue("VenueReview should have id < 1", artist.getReviews().get(0).getId() < 1);
        assertTrue("Gig should have id < 1", artist.getGigs().get(0).getId() < 1);
        assertTrue("GigRequest should have id < 1", artist.getRequests().get(0).getId() < 1);
        assertTrue("Member should have id < 1", artist.getMembers().get(0).getId() < 1);

        // Now persis the artist
        save(artist);

        // Make sure the children did not get persisted
        assertTrue("Venue should still have id < 1", artist.getVenues().get(0).getId() < 1);
        assertTrue("VenueReview should still have id < 1", artist.getReviews().get(0).getId() < 1);
        assertTrue("Gig should still have id < 1", artist.getGigs().get(0).getId() < 1);
        assertTrue("GigRequest should still have id < 1", artist.getRequests().get(0).getId() < 1);
        assertTrue("Member should still have id < 1", artist.getMembers().get(0).getId() < 1);
    }

    @Override
    public void testCascadeMerge() {
        // Acquire populated persisted artist
        Artist artist = getFullyPopulatedPersistedEntity(false);


        // Change properties of artist and children of artist
        artist.setName("Pocoyo");
        artist.getMembers().get(0).setUserName("Bono");
        artist.getReviews().get(0).setReview("suck me sideways");
        artist.getGigs().get(0).setDate("1992-08-23");
        artist.getVenues().get(0).setName("Redding Festival");
        artist.getRequests().get(0).setStatus(GigRequestStatus.STATUS_DISCOVERED);

        // Update the artist
        getRepository().save(artist);

        // Get child ids
        long reviewId = artist.getReviews().get(0).getId();
        long venueId = artist.getVenues().get(0).getId();
        long requestId = artist.getRequests().get(0).getId();
        long gigId = artist.getGigs().get(0).getId();
        long memberId = artist.getMembers().get(0).getId();

        // Verify the changes
        assertEquals("Review1 of artist should be \"suck me sideways\"", "suck me sideways", venueReviewRepo.getOne(reviewId).getReview());
        assertEquals("Venue 1 of artist should have name=\"Redding Festival\"", "Redding Festival", venueRepo.getOne(venueId).getName());
        assertEquals("Member 1 of artist should have name=\"Bono\"", "Bono", memberRepo.getOne(memberId).getUserName());
        assertEquals("Gig 1 of artist should have date =\"1992-08-23\"", "1992-08-23", gigRepository.getOne(gigId).getDate());
        assertEquals("Gig Request 1 of artist should have status=discovered", GigRequestStatus.STATUS_DISCOVERED, gigRequestRepo.getOne(requestId).getStatus());
    }

    @Override
    public void testCascadeDelete() {
        // Get fully persisted artist
        Artist artist = getFullyPopulatedPersistedEntity(true);

        // Remove the artist
        getRepository().delete(artist);

        VenueReview deletedReview = venueReviewRepo.getOne(artist.getReviews().get(0).getId());
        Gig deletedGig = gigRepository.getOne(artist.getGigs().get(0).getId());
        GigRequest deletedRequest = gigRequestRepo.getOne(artist.getRequests().get(0).getId());

        Venue venue = venueRepo.getOne(artist.getVenues().get(0).getId());
        Member member = memberRepo.getOne(artist.getMembers().get(0).getId());

        // Verify that Reviews, Gigs and GigRequest of the artist have been removed as well
        assertNull("Review with id=" + artist.getReviews().get(0).getId() + " in db anymore", deletedReview);
        assertNull("Gig with id=" + artist.getGigs().get(0).getId() + " in db anymore", deletedGig);
        assertNull("GigRequest with id=" + artist.getRequests().get(0).getId() + " in db anymore", deletedRequest);

        // Verify that the Members and Venues still exist in db
        assertNotNull("Venue with id=" + venue.getId() + " should still exist in db", venue);
        assertNotNull("member with id=" + member.getId() + " should still exist in db", member);
    }

    @Test
    public void testGetEntityWithChildren() {
        Artist fullyPopulatedArtist = getFullyPopulatedPersistedEntity(true);

        long persistedId = fullyPopulatedArtist.getId();

        Artist artist = getRepository().getArtistWithChildren(persistedId, true, true, true, true);
        assertNotNull("Reviews should exist", artist.getReviews());
        assertNotNull("Requests should exist", artist.getRequests());
        assertNotNull("Gigs should exist", artist.getGigs());
        assertNotNull("Venues should exist", artist.getVenues());

        // Test relation with members
        assertNotNull("Parent members should exist", artist.getMembers());

        boolean memberContainsOurArtist = false;
        for (Member member : artist.getMembers()) {
            if (member.getArtists().contains(artist)) {
                memberContainsOurArtist = true;
            }
        }
        // Relations to members is many-to-many, so we simply check that the artists members have at least one artist like ours
        assertTrue("at least one of members artists shoud match ours", memberContainsOurArtist);

        boolean venueContainsOurArtist = false;
        for (Venue venue : artist.getVenues()) {
            if (venue.getArtists().contains(artist)) {
                venueContainsOurArtist = true;
            }
        }
        // Relations to venues is many-to-many, so we simply check that the artists members have at least one artist like ours
        assertTrue("at least one of venues artists shoud match ours", venueContainsOurArtist);

        // Ensure the relations was bidirectional
        assertTrue("Review should have parent artist with id=" + persistedId, artist.getReviews().get(0).getArtist().getId() == persistedId);
        assertTrue("Request nr should have parent artist with id=" + persistedId, artist.getRequests().get(0).getArtist().getId() == persistedId);
        assertTrue("Gig nr should have parent artist with id=" + persistedId, artist.getGigs().get(0).getArtist().getId() == persistedId);
    }

    @Test
    public void testGetArtistsForMember() {
        List<Artist> artists = new ArrayList<Artist>();
        List<Member> members = new ArrayList<Member>();
        Artist artist1 = getRepository().save(getEntity1());
        Artist artist2 = getRepository().save(getEntity2());
        artists.add(artist1);
        artists.add(artist2);

        Member member = TestFixture.getValidMembersWithoutReferences().get(0);
        memberRepo.save(member);

        members.add(member);
        artist1.setMembers(members);
        artist2.setMembers(members);

        member.setArtists(artists); // persist 2 artists
        memberRepo.save(member);

        List<Artist> artistsForMember = getRepository().getArtistsForMember(member);
        assertEquals("There should be 2 artists where the member was our member", 2, artistsForMember.size());
    }

    @Override
    protected ArtistRepository getRepository() {
        return this.artistRepo;
    }

    @Override
    protected Artist getEntity1() {
        return TestFixture.getValidArtist("Taikes", "Gothenburg", "Sweden");
    }

    @Override
    protected Artist getEntity2() {
        return TestFixture.getValidArtist("Oasis", "Manchester", "UK");
    }

    @Override
    protected Artist getPersistedEntity1() {
        Artist artist = getEntity1();
        List<Artist> artists = new ArrayList<Artist>();
        artists.add(artist);

        artist.setMembers(TestFixture.getValidMembersWithoutReferences());
        for (Member member : artist.getMembers()) {
            member.setArtists(artists);
            this.memberRepo.save(member);
        }
        save(artist);
        return artist;
    }

    @Override
    protected Artist getPersistedEntity2() {
        Artist artist = getEntity2();

        List<Artist> artists = new ArrayList<Artist>();
        artists.add(artist);

        List<Member> members = new ArrayList<Member>();
        members.add(TestFixture.getValidMemberWithoutReferences("Jolo", "lizardKING", "Johan"));
        members.add(TestFixture.getValidMemberWithoutReferences("Hoho", "@$$h0Lie", "Jonas"));

        artist.setMembers(members);

        for (Member member : artist.getMembers()) {
            member.setArtists(artists);
            this.memberRepo.save(member);
        }
        return getRepository().saveAndFlush(artist);
    }

    @Override
    public Artist getFullyPopulatedPersistedEntity(boolean biDirectional) {
        Artist artist = getFullyPopulatedUnpersistedEntity(biDirectional);
        getRepository().save(artist);
        this.memberRepo.save(artist.getMembers().get(0));
        this.venueRepo.save(artist.getVenues().get(0));
        this.venueReviewRepo.save(artist.getReviews().get(0));
        this.gigRepository.save(artist.getGigs().get(0));
        this.gigRequestRepo.save(artist.getRequests().get(0));
        return artist;
    }

    @Override
    protected Artist getFullyPopulatedUnpersistedEntity(boolean biDirectional) {
        // Get an unpersisted artist
        Artist artist = TestFixture.getValidArtistWithEmptyReferences();

        // Set bi-directional relationships
        artist.addMember(TestFixture.getValidMemberWithoutReferences("FakeUsr", "Passw0rd", "Fake Name"));
        artist.addReview(TestFixture.getValidReviewWithoutReferences(artist.getName()));
        artist.addGig(TestFixture.getValidGigWithoutReferences());
        artist.addRequest(TestFixture.getValidRequestWithoutReferences(GigRequestStatus.STATUS_APPROVED));
        artist.addVenue(TestFixture.getValidVenueWithoutReferences("The Whiskey"));

        if (biDirectional) {

            // Set the relationships
            List<Artist> artists = new ArrayList<Artist>();
            artists.add(artist);

            Member member = artist.getMembers().get(0);
            member.setArtists(artists);
            member.setRelations();

            Venue venue = artist.getVenues().get(0);
            venue.setArtists(artists);
            venue.setReviews(artist.getReviews());
            venue.setAddress(TestFixture.getValidAddress());
            venue.setContacts(TestFixture.getValidContactsWithoutReferences());
            venue.setRelations();

            Gig gig = artist.getGigs().get(0);
            gig.setVenue(venue);
            gig.setArtist(artist);
            gig.setSchedule(TestFixture.getValidScheduleWithoutReference());
            gig.setSpecifics(TestFixture.getValidSpecificationsWithoutReference());
            gig.setRelations();

            VenueReview review = artist.getReviews().get(0);
            review.setArtist(artist);
            review.setVenue(venue);
            review.setRelations();

            GigRequest request = artist.getRequests().get(0);
            request.setVenue(venue);
            request.setArtist(artist);
            request.setRelations();

            artist = artists.get(0);
        }

        artist.setRelations();
        return artist;
    }
}
