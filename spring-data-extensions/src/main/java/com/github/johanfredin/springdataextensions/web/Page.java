package com.github.johanfredin.springdataextensions.web;

/**
 * Holds urls for the view pages
 *
 * @author johan
 */
public class Page {

    public static final String INDEX = "/index/member={memberId}";
    public static final String INDEX_FW = "index";
    public static final String REGISTER_MEMBER = "/registerMember";
    public static final String REGISTER_MEMBER_FW = "registerMember";
    public static final String LOGIN = "/login";
    public static final String LOGIN_FW = "login";
    public static final String RESET_PASSWORD = "/resetPassword";
    public static final String RESET_PASSWORD_FW = "resetPassword";
    public static final String EDIT_ARTIST = "/editArtist/member={memberId}/artist={artistId}";
    public static final String EDIT_ARTIST_FW = "editArtist";
    public static final String EDIT_MEMBER_FW = "editMember";
    public static final String EDIT_MEMBER = "/editMember/member={memberId}";
    public static final String ADD_MEMBERS_TO_ARTIST = "/addMembersToArtist/member={memberId}/artist={artistId}/invite={inviteId}";
    public static final String ADD_MEMBERS_TO_ARTIST_FW = "addMembersToArtist";
    public static final String EDIT_ARTIST_REVIEWS = "/editArtistReviews/artist={artistId}/member={memberId}";
    public static final String EDIT_ARTIST_REVIEWS_FW = "editArtistReviews";
    public static final String EDIT_VENUE = "/editVenue/venue={venueId}/artist={artistId}/member={memberId}";
    public static final String EDIT_VENUE_FW = "editVenue";
    public static final String EDIT_VENUE_REVIEW = "/editVenueReviews/venue={venueId}/artist={artistId}/member={memberId}/review={reviewId}";
    public static final String EDIT_VENUE_REVIEW_FW = "editVenueReviews";
    public static final String FINDE_VENUES = "/findVenues/artist={artistId}/member={memberId}";
    public static final String FIND_VENUES_FW = "findVenues";

}
