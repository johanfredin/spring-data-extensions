package com.github.johanfredin.springdataextensions.domain;

import com.github.johanfredin.springdataextensions.TestFixture;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ArtistTest extends EntityTest<Artist> {

    @Override
    public void testDefaultConstructor() {
        Artist artist = getEmptyInstance();
        assertEquals("Artist id should be 0", 0L, artist.getId());
        assertNull("Artist name should be empty", artist.getName());
        assertNull("Artist city should be empty", artist.getCity());
        assertNull("Artist country should be empty", artist.getCountry());
        assertNull("Artist bio should be empty", artist.getBio());
        assertNull("Artist official website should be empty", artist.getOfficialWebsite());
        assertNull("Artist youtube address should be empty", artist.getYoutubeAddress());
        assertNull("Artist facebook address should be empty", artist.getFacebookAddress());
        assertNull("Artist spotify address should be empty", artist.getSpotifyAddress());
        assertNull("Artist iTunes address should be empty", artist.getiTunesAddress());
        assertNull("Artist email should be empty", artist.getEmail());
        assertNull("Artist should have no genre yet", artist.getGenre());
        assertNull("Artist should have no venues yet", artist.getVenues());
        assertNull("Artist should have no requests yet", artist.getRequests());
        assertNull("Artist should have no gigs yet", artist.getGigs());
        assertNull("Artist should have no members yet", artist.getMembers());
        assertNull("Artist should have no reviews yet", artist.getReviews());
    }

    @Override
    public void testConstructorWithParameters() {
        Artist artist = getPopulatedInstance();
        assertEquals("Artist name should be " + TestFixture.FAKE_BANDNAME, TestFixture.FAKE_BANDNAME, artist.getName());
        assertEquals("Artist city should be " + TestFixture.FAKE_CITY, TestFixture.FAKE_CITY, artist.getCity());
        assertEquals("Artist country should be " + TestFixture.FAKE_COUNTRY, TestFixture.FAKE_COUNTRY, artist.getCountry());
        assertEquals("Artist bio should be " + TestFixture.FAKE_BANDNAME + " bio", TestFixture.FAKE_BANDNAME + " bio", artist.getBio());
        assertEquals("Artist official website should be " + TestFixture.getUrl(TestFixture.FAKE_BANDNAME), TestFixture.getUrl(TestFixture.FAKE_BANDNAME), artist.getOfficialWebsite());
        assertEquals("Artist youtube address should be " + TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "youtube"), TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "youtube"), artist.getYoutubeAddress());
        assertEquals("Artist facebook address should be " + TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "facebook"), TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "facebook"), artist.getFacebookAddress());
        assertEquals("Artist spotify address should be " + TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "spotify"), TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "spotify"), artist.getSpotifyAddress());
        assertEquals("Artist iTunes address should be " + TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "itunes"), TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "itunes"), artist.getiTunesAddress());
        assertEquals("Artist email should be " + TestFixture.getFakeMail(TestFixture.FAKE_BANDNAME), TestFixture.getFakeMail(TestFixture.FAKE_BANDNAME), artist.getEmail());
        assertEquals("Artist genre should be " + TestFixture.FAKE_GENRE, TestFixture.FAKE_GENRE, artist.getGenre());
        assertNotNull("Artist should now have venues", artist.getVenues());
        assertNotNull("Artist should now have requests", artist.getRequests());
        assertNotNull("Artist should now have gigs", artist.getGigs());
        assertNotNull("Artist should now have members", artist.getMembers());
        assertNotNull("Artist should now have reviews", artist.getReviews());
    }

    @Override
    public void testRelations() {
        Artist artist = getPopulatedInstance();
        artist.setRelations();
        for (VenueReview review : artist.getReviews()) {
            assertNotNull("Review should now have an artist", review.getArtist());
            assertTrue("Review should have an artist same as artist that sat the relations", review.getArtist().equals(artist));
        }

        for (GigRequest request : artist.getRequests()) {
            assertNotNull("Request should now have an artist", request.getArtist());
            assertTrue("Request should have an artist same as artist that sat the relations", request.getArtist().equals(artist));
        }

        for (Gig gig : artist.getGigs()) {
            assertNotNull("Gig should now have an artist", gig.getArtist());
            assertTrue("Gig should have an artist same as artist that sat the relations", gig.getArtist().equals(artist));
        }
    }

    @Override
    public void testCopyData() {
        Artist emptyArtist = getEmptyInstance();
        Artist populatedArtist = getPopulatedInstance();
        populatedArtist.setId(TestFixture.FAKE_ID);

        emptyArtist.copyDataFromEntity(populatedArtist);

        assertEquals("Artist id should now be " + TestFixture.FAKE_ID, TestFixture.FAKE_ID, emptyArtist.getId());
        assertEquals("Artist name should be " + TestFixture.FAKE_BANDNAME, TestFixture.FAKE_BANDNAME, emptyArtist.getName());
        assertEquals("Artist city should be " + TestFixture.FAKE_CITY, TestFixture.FAKE_CITY, emptyArtist.getCity());
        assertEquals("Artist country should be " + TestFixture.FAKE_COUNTRY, TestFixture.FAKE_COUNTRY, emptyArtist.getCountry());
        assertEquals("Artist bio should be " + TestFixture.FAKE_BANDNAME + " bio", TestFixture.FAKE_BANDNAME + " bio", emptyArtist.getBio());
        assertEquals("Artist official website should be " + TestFixture.getUrl(TestFixture.FAKE_BANDNAME), TestFixture.getUrl(TestFixture.FAKE_BANDNAME), emptyArtist.getOfficialWebsite());
        assertEquals("Artist youtube address should be " + TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "youtube"), TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "youtube"), emptyArtist.getYoutubeAddress());
        assertEquals("Artist facebook address should be " + TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "facebook"), TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "facebook"), emptyArtist.getFacebookAddress());
        assertEquals("Artist spotify address should be " + TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "spotify"), TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "spotify"), emptyArtist.getSpotifyAddress());
        assertEquals("Artist iTunes address should be " + TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "itunes"), TestFixture.getUrl(TestFixture.FAKE_BANDNAME, "itunes"), emptyArtist.getiTunesAddress());
        assertEquals("Artist email should be " + TestFixture.getFakeMail(TestFixture.FAKE_BANDNAME), TestFixture.getFakeMail(TestFixture.FAKE_BANDNAME), emptyArtist.getEmail());
        assertEquals("Artist genre should be " + TestFixture.FAKE_GENRE, TestFixture.FAKE_GENRE, emptyArtist.getGenre());
        assertNotNull("Artist should now have venues", emptyArtist.getVenues());
        assertNotNull("Artist should now have requests", emptyArtist.getRequests());
        assertNotNull("Artist should now have gigs", emptyArtist.getGigs());
        assertNotNull("Artist should now have members", emptyArtist.getMembers());
        assertNotNull("Artist should now have reviews", emptyArtist.getReviews());
    }

    @Override
    protected Artist getEmptyInstance() {
        return new Artist();
    }

    @Override
    protected Artist getPopulatedInstance() {
        return TestFixture.getValidArtist(TestFixture.FAKE_BANDNAME, TestFixture.FAKE_CITY, TestFixture.FAKE_COUNTRY, TestFixture.getValidMembers());
    }


}