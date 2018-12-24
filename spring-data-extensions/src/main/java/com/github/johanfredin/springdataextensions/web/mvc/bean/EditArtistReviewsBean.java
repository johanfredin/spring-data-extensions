package com.github.johanfredin.springdataextensions.web.mvc.bean;

import javax.validation.Valid;
import java.util.List;

public class EditArtistReviewsBean extends Bean<VenueReview> {

    private long artistId;

    @Valid
    private List<VenueReview> reviews;

    public EditArtistReviewsBean() {
        super(0L);
    }

    public EditArtistReviewsBean(long memberId, long artistId, List<VenueReview> reviews) {
        super(memberId);
        setArtistId(artistId);
        setReviews(reviews);
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public List<VenueReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<VenueReview> reviews) {
        this.reviews = reviews;
    }

    @Override
    public VenueReview getEntity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEntity(VenueReview entity) {
        // TODO Auto-generated method stub

    }


}
