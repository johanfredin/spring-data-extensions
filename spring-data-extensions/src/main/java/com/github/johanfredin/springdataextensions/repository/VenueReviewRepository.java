package com.github.johanfredin.springdataextensions.repository;

import com.github.johanfredin.springdataextensions.util.RepositoryUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Primary
public interface VenueReviewRepository extends BaseRepository<VenueReview> {

    /**
     * Get all the reviews at given {@link Venue}
     *
     * @param venue the venue to fetch the reviews from
     * @return all the reviews for the passed in venue
     */
    List<VenueReview> findAllByVenue(Venue venue); // replaces List<VenueReview> getReviewsForVenue(Venue venue);

    /**
     * Get all the reviews written by given {@link Artist}
     *
     * @param artist the artist to fetch the reviews from
     * @return all the reviews from the passed in artist
     */
    List<VenueReview> findAllByArtist(Artist artist); // replacesList<VenueReview> getReviewsFromArtist(Artist artist);

    /**
     * Get a limited amount of reviews at given {@link Venue}
     *
     * @param venue the venue to fetch the reviews from
     * @param limit the max amount of reviews to return
     * @return a limited the reviews for the passed in venue
     */
    Page<VenueReview> findAllByVenue(Venue venue, Pageable pageable); // List<VenueReview> getReviewsForVenue(Venue venue, int limit);

    /**
     * Get a limited amount of reviews written by given {@link Artist}
     *
     * @param artist the artist to fetch the reviews from
     * @param limit  the max amount of reviews to return
     * @return all the reviews from the passed in artist
     */
    Page<VenueReview> findAllByArtist(Artist artist, Pageable pageable); // replace List<VenueReview> getReviewsFromArtist(Artist artist, int limit);

    /**
     * Get a review for a {@link Venue} by a certain {@link Artist}
     *
     * @param artist the artist who wrote the review
     * @param venue  the venue the review is written for
     * @return a review for a {@link Venue} by a certain {@link Artist}
     */
    VenueReview findOneByArtistAndVenue(Artist artist, Venue venue); // replaces VenueReview getReviewFromArtistAtVenue(Artist artist, Venue venue);

    /**
     * @param reviewId
     * @return the review with the matching id with or without initialized {@link Venue}
     */
    @Query("select vr from VenueReview vr inner join vr.venue v where vr.id=:id")
    VenueReview getReviewWithVenue(@Param("id") long reviewId);

    /**
     * @param reviewId
     * @return the review with the matching id with or without initialized {@link Artist}
     */
    @Query("select vr from VenueReview vr inner join vr.artist a where vr.id=:id")
    VenueReview getReviewWithArtist(@Param("id") long reviewId);

    /**
     * @param reviewId
     * @return the review with the matching id with or without initialized {@link Artist}
     */
    @Query("select vr from VenueReview vr inner join vr.artist a inner join vr.venue v where vr.id=:id")
    VenueReview getReviewWithArtistAndVenue(@Param("id") long reviewId);

    /**
     * Get a review and initialize children if chosen.
     *
     * @param reviewId      id of the review
     * @param isFetchVenue  whether or not to initialize the {@link Venue} of the review
     * @param isFetchArtist whether or not to initialize the {@link Artist} of the review
     * @return the review with the matching id with or without initialized children
     */
    default VenueReview getReviewWithChildren(long reviewId, boolean isFetchVenue, boolean isFetchArtist) {
        switch (RepositoryUtil.getBooleanArgsAsString(isFetchVenue, isFetchArtist)) {
            case "10":
                return getReviewWithVenue(reviewId);
            case "01":
                return getReviewWithArtist(reviewId);
        }
        return getReviewWithArtistAndVenue(reviewId);
    }

    /**
     * @param venue
     * @param artist
     * @return <code>true</code> if passed in artist has already written a review for passed in venue
     */
    boolean existsByVenueAndArtist(Venue venue, Artist artist); // replaces boolean isReviewAlreadyWrittenForVenue(Venue venue, Artist artist);
}
