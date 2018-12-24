package com.github.johanfredin.springdataextensions.domain;

import com.github.johanfredin.springdataextensions.TestFixture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class GigRequestTest extends EntityTest<GigRequest> {

    @Test
    public void testDefaultConstructor() {
        GigRequest request = getEmptyInstance();
        assertEquals("Id should be 0", 0, request.getId());
        assertNull("Request date should be empty", request.getRequestDate());
        assertEquals("Status should be NOT_SET", GigRequestStatus.STATUS_NOT_SET, request.getStatus());
        assertNull("Venue should be null", request.getVenue());
        assertNull("Artist should be null", request.getArtist());
        assertEquals("Last change user Id should be 0", 0L, request.getLastChangeUserId());
    }

    @Override
    public void testConstructorWithParameters() {
        GigRequest request = getPopulatedInstance();
        assertEquals("Request date should be " + TestFixture.FAKE_DATE, TestFixture.FAKE_DATE, request.getRequestDate());
        assertEquals("Status should be APPROVED", GigRequestStatus.STATUS_APPROVED, request.getStatus());
        assertNotNull("Request should have a venue", request.getVenue());
        assertNotNull("Request should have an artist", request.getArtist());
        assertEquals("Last chagne user id should be 1", 1L, request.getLastChangeUserId());
        assertEquals("Last change date should be " + TestFixture.FAKE_DATE, TestFixture.FAKE_DATE, request.getRequestDate());
    }


    @Override
    public void testRelations() {
        List<GigRequest> validRequests = TestFixture.getValidRequests();
        for (GigRequest request : validRequests) {
            request.setArtist(null);
        }

        Artist artist = TestFixture.getValidArtist();
        artist.setRequests(validRequests);
        artist.setRelations();

        for (GigRequest request : validRequests) {
            assertNotNull("Request should now have an artist", request.getArtist());
            assertTrue("Request should equal the Request of the artist", artist.equals(request.getArtist()));
        }
    }


    @Override
    public void testCopyData() {
        GigRequest populatedRequest = getPopulatedInstance();
        populatedRequest.setId(666L);

        GigRequest emptyRequest = getEmptyInstance();

        emptyRequest.copyDataFromEntity(populatedRequest);
        assertEquals("Request should have an id with 666", 666L, emptyRequest.getId());
        assertEquals("Request date should be " + TestFixture.FAKE_DATE, TestFixture.FAKE_DATE, emptyRequest.getRequestDate());
        assertEquals("Status should be APPROVED", GigRequestStatus.STATUS_APPROVED, emptyRequest.getStatus());
        assertNotNull("Request should have a venue", emptyRequest.getVenue());
        assertNotNull("Request should have an artist", emptyRequest.getArtist());
        assertEquals("Last chagne user id should be 1", 1L, emptyRequest.getLastChangeUserId());
        assertEquals("Last change date should be " + TestFixture.FAKE_DATE, TestFixture.FAKE_DATE, emptyRequest.getRequestDate());
    }

    @Override
    protected GigRequest getEmptyInstance() {
        return new GigRequest();
    }

    @Override
    protected GigRequest getPopulatedInstance() {
        return TestFixture.getValidRequest(GigRequestStatus.STATUS_APPROVED);
    }

}
