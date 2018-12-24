package com.github.johanfredin.springdataextensions.web.mvc.bean;

import com.github.johanfredin.springdataextensions.domain.*;
import com.pocstage.venuehub.domain.*;
import com.github.johanfredin.springdataextensions.util.comparator.DateComparator;

import javax.validation.Valid;
import java.util.List;

public class EditVenueBean extends Bean<Venue> {

    @Valid
    private Venue venue;

    @Valid
    private Address address;

    @Valid
    private List<ContactPerson> contacts;

    @Valid
    private List<VenueReview> reviews;

    @Valid
    private List<Artist> artists;

    private long artistId;

    /**
     * Construct a new bean instance and call {@link #copyDataFromDBEntity(Venue)} passing in the parameter
     *
     * @param venue the venue to populate the bean with (can be <code>null</code>)
     */
    public EditVenueBean(Venue venue, long memberId, long artistId) {
        this(venue, memberId, artistId, null);
    }

    /**
     * Construct a new bean instance and call {@link #copyDataFromDBEntity(Venue)} passing in the parameter
     *
     * @param venue the venue to populate the bean with (can be <code>null</code>)
     */
    public EditVenueBean(Venue venue, long memberId, long artistId, DateComparator dateComparator) {
        super(memberId);
        setArtistId(artistId);
        setDateComparator(dateComparator);
        copyDataFromDBEntity(venue);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<ContactPerson> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactPerson> contacts) {
        this.contacts = contacts;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public List<VenueReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<VenueReview> reviews) {
        this.reviews = reviews;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    /**
     * attaches all the children to the venue
     */
    public void setRelations() {
        getVenue().setAddress(getAddress());
        getVenue().setArtists(getArtists());
        getVenue().setContacts(getContacts());
        getVenue().setReviews(getReviews());
    }

    public void copyDataFromDBEntity(Venue venue) {
        setVenue(venue);
        setAddress(venue.getAddress());
        setContacts(venue.getContacts());
        setArtists(venue.getArtists());
        setReviews(venue.getReviews());
        setRelations();
    }

    public Venue getPopulatedEntityFromBean() {
        setRelations();
        return getVenue();
    }

    @Override
    public Venue getEntity() {
        return this.venue;
    }

    @Override
    public void setEntity(Venue entity) {
        this.venue = entity;
    }

    @Override
    public String toString() {
        return "EditVenueBean [\nvenue=" + venue + ", \naddress=" + address + ", \ncontacts=" + contacts
                + ", \nreviews=" + reviews + ", \nartists=" + artists + ", \nartistId=" + artistId + "]";
    }


}
