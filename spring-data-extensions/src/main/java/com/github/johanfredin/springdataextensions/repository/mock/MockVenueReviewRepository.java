package com.github.johanfredin.springdataextensions.repository.mock;

import com.github.johanfredin.springdataextensions.repository.VenueReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class MockVenueReviewRepository extends MockRepository<VenueReview> implements VenueReviewRepository {

    @Override
    public List<VenueReview> findAllByVenue(Venue venue) {
        return venue.getReviews();
    }

    @Override
    public List<VenueReview> findAllByArtist(Artist artist) {
        return artist.getReviews();
    }

    @Override
    public Page<VenueReview> findAllByVenue(Venue venue, Pageable pageable) {
        return null;
    }

    @Override
    public Page<VenueReview> findAllByArtist(Artist artist, Pageable pageable) {
        return null;
    }

    @Override
    public VenueReview findOneByArtistAndVenue(Artist artist, Venue venue) {
        for (VenueReview vr : findAll()) {
            if (vr.getArtist().equals(artist) && vr.getVenue().equals(venue)) {
                return vr;
            }
        }
        return null;
    }

    @Override
    public VenueReview getReviewWithVenue(long reviewId) {
        return getOne(reviewId);
    }

    @Override
    public VenueReview getReviewWithArtist(long reviewId) {
        return getOne(reviewId);
    }

    @Override
    public VenueReview getReviewWithArtistAndVenue(long reviewId) {
        return getOne(reviewId);
    }

    @Override
    public boolean existsByVenueAndArtist(Venue venue, Artist artist) {
        for (VenueReview vr : artist.getReviews()) {
            if (vr.getVenue().equals(venue)) {
                return true;
            }
        }
        return false;
    }


}
