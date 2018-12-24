package com.github.johanfredin.springdataextensions.domain;

import com.github.johanfredin.springdataextensions.TestFixture;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class VenueReviewTest extends EntityTest<VenueReview> {

    @Override
    public void testDefaultConstructor() {
        VenueReview review = getEmptyInstance();
        assertEquals("Id should be 0", 0L, review.getId());
        assertEquals("Score should be 0", 0, review.getScore());
        assertNull("Review should be null", review.getReview());
        assertNull("Artist should be null", review.getArtist());
        assertNull("Venue should be null", review.getVenue());
    }

    @Override
    public void testConstructorWithParameters() {
        VenueReview review = TestFixture.getValidReview(TestFixture.getValidArtist("Taikes", "Gothenburg", "Sweden"));
        assertEquals("Score should be 10", 10, review.getScore());
        assertEquals("Review should be " + review.getArtist().getName() + " thinks this place is awesome",
                review.getArtist().getName() + " thinks this place is awesome", review.getReview());
        assertNotNull("Review should have an artist", review.getArtist());
        assertNotNull("Review should have a venue", review.getVenue());
    }

    @Override
    public void testRelations() {
        List<VenueReview> validReviews = TestFixture.getValidReviews();
        for (VenueReview review : validReviews) {
            review.setArtist(null);
            review.setVenue(null);
        }

        Artist validArtist = TestFixture.getValidArtist("Taikes", "Gothenburg", "Sweden");
        validArtist.setReviews(null);
        validArtist.setReviews(validReviews);

        Venue validVenue = TestFixture.getValidVenue("The Whiskey");
        validVenue.setReviews(null);
        validVenue.setReviews(validReviews);

        validArtist.setRelations();
        validVenue.setRelations();

        assertNotNull("Artist should now have reviews", validArtist.getReviews());
        assertNotNull("Venue should now have reviews", validVenue.getReviews());
    }

    @Override
    public void testCopyData() {
        VenueReview emptyReview = new VenueReview();

        VenueReview populatedReview = TestFixture.getValidReview(TestFixture.getValidArtist("Taikes", "Gothenburg", "Sweden"));
        populatedReview.setId(666L);

        emptyReview.copyDataFromEntity(populatedReview);


        assertEquals("Score should be 10", 10, emptyReview.getScore());
        assertEquals("Review should be " + emptyReview.getArtist().getName() + " thinks this place is awesome",
                emptyReview.getArtist().getName() + " thinks this place is awesome", emptyReview.getReview());
        assertNotNull("Review should have an artist", emptyReview.getArtist());
        assertNotNull("Review should have a venue", emptyReview.getVenue());
    }


    @Override
    protected VenueReview getEmptyInstance() {
        return new VenueReview();
    }

    @Override
    protected VenueReview getPopulatedInstance() {
        return TestFixture.getValidReview();
    }

}
