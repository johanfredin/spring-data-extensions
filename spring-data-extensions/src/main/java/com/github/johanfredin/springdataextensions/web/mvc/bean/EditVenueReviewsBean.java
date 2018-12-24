package com.github.johanfredin.springdataextensions.web.mvc.bean;

import javax.validation.Valid;

public class EditVenueReviewsBean extends Bean<VenueReview> {

    @Valid
    private VenueReview review;

    private long venueId;
    private long artistId;

    public EditVenueReviewsBean(VenueReview review, long memberId, long artistId, long venueId) {
        super(review, memberId);
        setArtistId(artistId);
        setVenueId(venueId);
    }

    @Override
    public VenueReview getEntity() {
        return review;
    }

    @Override
    public void setEntity(VenueReview entity) {
        this.review = entity;
    }

    public VenueReview getReview() {
        return review;
    }

    public void setReview(VenueReview review) {
        this.review = review;
    }

    public long getVenueId() {
        return venueId;
    }

    public void setVenueId(long venueId) {
        this.venueId = venueId;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }


}
