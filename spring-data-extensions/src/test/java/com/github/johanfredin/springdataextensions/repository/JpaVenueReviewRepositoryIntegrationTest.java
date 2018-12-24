package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.TestFixture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
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
public class JpaVenueReviewRepositoryIntegrationTest extends AbstractJpaRepositoryTest<VenueReview, VenueReviewRepository> {

    @Autowired
    private VenueReviewRepository venueReviewRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Override
    protected VenueReviewRepository getRepository() {
        return this.venueReviewRepository;
    }

    @Override
    protected VenueReview getEntity1() {
        VenueReview review = TestFixture.getValidReviewWithoutReferences(TestFixture.FAKE_BANDNAME);
        review.setVenue(TestFixture.getValidVenueWithoutReferences(TestFixture.FAKE_VENUE_NAME));
        review.setArtist(TestFixture.getValidArtist());
        this.venueRepository.save(review.getVenue());
        this.artistRepository.save(review.getArtist());
        return review;
    }

    @Override
    protected VenueReview getEntity2() {
        VenueReview review = TestFixture.getValidReviewWithoutReferences("Oasis");
        review.setArtist(TestFixture.getValidArtist("Oasis", "Manchester", "UK"));
        review.setVenue(TestFixture.getValidVenueWithoutReferences("Obadoba"));
        this.venueRepository.save(review.getVenue());
        this.artistRepository.save(review.getArtist());
        return review;
    }

    @Override
    protected VenueReview getFullyPopulatedUnpersistedEntity(boolean biDirectional) {
        VenueReview review = TestFixture.getValidReviewWithoutReferences("Nirvana");
        review.setArtist(TestFixture.getValidArtist());
        review.setVenue(TestFixture.getValidVenueWithoutReferences(TestFixture.FAKE_VENUE_NAME));
        if (biDirectional) {
            review.getArtist().setReviews(new ArrayList<VenueReview>());
            review.getVenue().setReviews(new ArrayList<VenueReview>());
            review.setRelations();
        }
        return review;
    }

    @Override
    protected VenueReview getFullyPopulatedPersistedEntity(boolean biDirectional) {
        VenueReview review = getFullyPopulatedUnpersistedEntity(biDirectional);
        this.artistRepository.save(review.getArtist());
        this.venueRepository.save(review.getVenue());
        return getRepository().save(review);
    }

    @Override
    public void testCascadePersist() {
        // VenueReview has no cascade persisting, we should verify that here
        VenueReview review = getRepository().save(getFullyPopulatedUnpersistedEntity(true));

        // Verify that the artist and the venue are not persisted
        assertFalse("artist of review should not be persisted", review.getArtist().isExistingEntity());
        assertFalse("venue of review should not be persisted", review.getVenue().isExistingEntity());
    }

    @Override
    public void testCascadeMerge() {
        VenueReview review = getFullyPopulatedPersistedEntity(true);
        String currentText = review.getReview();

        Artist artist = review.getArtist();
        Venue venue = review.getVenue();

        // Verify parents have the same text
        assertTrue("Venue and Artist of review should have text=" + currentText,
                venue.getReviews().get(0).getReview().equals(currentText) &&
                        artist.getReviews().get(0).getReview().equals(currentText));

        // Set a new review text
        String newText = "Aholalola this is shite";
        review.setReview(newText);

        // Now update the review in db
        getRepository().save(review);

        Artist dbArtist = this.artistRepository.getOne(review.getArtist().getId());
        Venue dbVenue = this.venueRepository.getOne(review.getVenue().getId());

        // Verify parents have the same text
        assertTrue("after merge Venue and Artist of review should have text=" + newText,
                dbVenue.getReviews().get(0).getReview().equals(newText) &&
                        dbArtist.getReviews().get(0).getReview().equals(newText));

    }

    @Override
    public void testCascadeDelete() {
        // Deleting a review has no cascade effect, veryfy that
        VenueReview review = getFullyPopulatedPersistedEntity(true);
        getRepository().delete(review);

        // Verify artist and venue of review still exist
        assertNotNull("Artist that wrote the review should still exist in db", this.artistRepository.getOne(review.getArtist().getId()));
        assertNotNull("Venue which the review was written for should still exist in db", this.venueRepository.getOne(review.getVenue().getId()));
    }

    @Test
    public void testFindAllByVenue() {
        List<VenueReview> reviewsByVenue = getRepository().findAllByVenue(getFullyPopulatedPersistedEntity(true).getVenue());
        assertEquals("Should result in 1 match", 1, reviewsByVenue.size());
    }

    @Test
    public void testFindAllByArtist() {
        List<VenueReview> reviewsByArtist = getRepository().findAllByArtist(getFullyPopulatedPersistedEntity(true).getArtist());
        assertEquals("Should result in 1 match", 1, reviewsByArtist.size());
    }

    @Test
    public void testFindLimitedByVenue() {
        List<VenueReview> reviews = persistEntities1And2();

        Page<VenueReview> byVenue = getRepository().findAllByVenue(reviews.get(0).getVenue(), new PageRequest(0, 1));
        assertEquals("Result should be 1", 1, byVenue.getContent().size());
    }

    @Test
    public void testFindLimitedByArtist() {
        List<VenueReview> reviews = persistEntities1And2();

        Page<VenueReview> byArtist = getRepository().findAllByArtist(reviews.get(0).getArtist(), new PageRequest(0, 1));
        assertEquals("Result should be 1", 1, byArtist.getContent().size());
    }

    @Test
    public void testFindOneByArtistAndVenue() {
        VenueReview review = getFullyPopulatedPersistedEntity(true);
        VenueReview reviewByArtistAndVenue = getRepository().findOneByArtistAndVenue(review.getArtist(), review.getVenue());
        assertNotNull("Review should not be null", reviewByArtistAndVenue);
    }

    @Test
    public void testGetVenueReviewWithChildren() {
        long id = getFullyPopulatedPersistedEntity(true).getId();
        VenueReview review = getRepository().getReviewWithChildren(id, true, true);
        assertNotNull("Artist of review should not be null", review.getArtist());
        assertNotNull("Venue of review should not be null", review.getVenue());
    }

    @Test
    public void testExistsByVenueAndArtist() {
        VenueReview review = getFullyPopulatedPersistedEntity(true);
        Venue venue = review.getVenue();
        Artist artist = review.getArtist();
        boolean isWritten = getRepository().existsByVenueAndArtist(venue, artist);
        assertTrue("Review should be written for venue by artist", isWritten);
    }

}
