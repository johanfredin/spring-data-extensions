package com.github.johanfredin.springdataextensions.service;

import com.github.johanfredin.springdataextensions.repository.VenueReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VenueReviewService extends ServiceBase<VenueReview, VenueReviewRepository> {

    default List<VenueReview> findAllByVenue(Venue venue) {
        return getRepository().findAllByVenue(venue);
    }

    default List<VenueReview> findAllByArtist(Artist artist) {
        return getRepository().findAllByArtist(artist);
    }

    default Page<VenueReview> findAllByVenue(Venue venue, Pageable pageable) {
        return getRepository().findAllByVenue(venue, pageable);
    }

    default Page<VenueReview> findAllByArtist(Artist artist, Pageable pageable) {
        return getRepository().findAllByArtist(artist, pageable);
    }

    default VenueReview findOneByArtistAndVenue(Artist artist, Venue venue) {
        return getRepository().findOneByArtistAndVenue(artist, venue);
    }

    default VenueReview getReviewWithVenue(long reviewId) {
        return getRepository().getReviewWithVenue(reviewId);
    }

    default VenueReview getReviewWithArtist(long reviewId) {
        return getRepository().getReviewWithArtist(reviewId);
    }

    default VenueReview getReviewWithArtistAndVenue(long reviewId) {
        return getRepository().getReviewWithArtistAndVenue(reviewId);
    }

    default VenueReview getReviewWithChildren(long reviewId, boolean isFetchVenue, boolean isFetchArtist) {
        return getRepository().getReviewWithChildren(reviewId, isFetchVenue, isFetchArtist);
    }

    default boolean existsByVenueAndArtist(Venue venue, Artist artist) {
        return getRepository().existsByVenueAndArtist(venue, artist);
    }

}
