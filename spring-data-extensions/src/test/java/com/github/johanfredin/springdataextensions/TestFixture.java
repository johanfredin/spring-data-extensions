package com.github.johanfredin.springdataextensions;

import com.github.johanfredin.springdataextensions.constants.*;
import com.github.johanfredin.springdataextensions.domain.*;
import com.pocstage.venuehub.constants.*;
import com.pocstage.venuehub.domain.*;

import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestFixture {

    public static final long FAKE_ID = 666L;
    public static final String FAKE_LAST_UPDATE = "1991-09-23 23:00:35";
    public static final String FAKE_PHONE = "123456789";
    public static final String FAKE_DATE = "2015-09-25";
    public static final String FAKE_TIME = "20:00";
    public static final String FAKE_USERNAME = "zolost";
    public static final String FAKE_PASSWORD = "lizardKING";
    public static final String FAKE_NAME = "Johan";
    public static final String FAKE_EMAIL = "info@" + FAKE_USERNAME.toLowerCase().trim().replaceAll(" ", "") + ".com";
    public static final String FAKE_BANDNAME = "Taikes";
    public static final String FAKE_CITY = "Gothenburg";
    public static final String FAKE_COUNTRY = "Sweden";
    public static final String FAKE_VENUE_NAME = "The Whiskey";
    public static final String FAKE_MESSENGER_ARTIST = "Artist";
    public static final String FAKE_MESSENGER_VENUE = "Venue";
    public static final String FAKE_MESSENGER_CONTACTPERSON = "ContactPerson";
    public static final String FAKE_MESSENGER_MEMBER = "Member";
    public static final String FAKE_MESSEGE_TO_VENUE = "Hey we would love to play at your venue!";
    public static final String FAKE_RESPONSE_FROM_VENUE = "You ain't playin here! You suck!";
    public static final String FAKE_RESPONSE_FROM_MEMBER = "No thanks, we heard bad stories about your Venue!";
    public static final String FAKE_MESSAGE_FROM_VENUE = "Would you like to perform at our Venue?";
    public static final String FAKE_MESSAGE_TO_MEMBER = "Would you like to join my band?";
    public static final Genre FAKE_GENRE = Genre.GRUNGE_ROCK;
    public static final MemberRole FAKE_ROLE = MemberRole.ARTIST;
    public static final int LIST_SIZE = 2;
    public static final int INDEX_1 = 0;
    public static final int INDEX_2 = 1;
    public static final byte FAKE_CAPACITY = 100;

    public static String getUrl(String name, String site) {
        return "http://www." + site.toLowerCase() + ".com" + "/" + name.trim().replace(" ", "").toLowerCase();
    }

    public static String getUrl(String name) {
        return "http://www." + name.toLowerCase().trim().replaceAll(" ", "") + ".com";
    }

    public static final String getFakeMail(String name) {
        return "info@" + name.toLowerCase().trim().replaceAll(" ", "") + ".com";
    }

    public static List<Member> getBiDirectionalMembersForArtist(Artist artist, Member... members) {
        List<Artist> artistForMembers = new ArrayList<Artist>();
        artistForMembers.add(artist);
        List<Member> membersForArtist = new ArrayList<Member>();
        membersForArtist.addAll(Arrays.asList(members));
        for (Member member : membersForArtist) {
            if (member.getArtists() != null) {
                if (!member.getArtists().isEmpty()) {
                    member.addArtist(artist);
                }
            } else {
                member.setArtists(artistForMembers);
            }
        }
        artist.setMembers(membersForArtist);
        return membersForArtist;
    }

    // -----------------------------------------------------------------------------------------------------------------------
    // ------------------------------ ADDRESS --------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------

    public static Address getValidAddress() {
        return getValidAddress("Street", "City", "Zippie", "Region", "Country");
    }

    public static Address getValidAddress(String street, String city, String zipCode, String region, String country) {
        return new Address(street, city, zipCode, region, country, Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LONGITUDE);
    }

    // -----------------------------------------------------------------------------------------------------------------------
    // ------------------------------ CONTACT PERSON -------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------

    public static ContactPerson getValidContactPerson() {
        return getValidContactPerson(FAKE_NAME);
    }

    /**
     * @return a new {@link ContactPerson} with all fields filled in with dummy values
     */
    public static ContactPerson getValidContactPerson(String name) {
        return new ContactPerson(name, getFakeMail(name), FAKE_PHONE, "Gothenburg", "Sweden", MemberRole.NOT_SET, getValidVenue());
    }

    public static ContactPerson getValidContactPersonWithoutReferences(String name) {
        return new ContactPerson(name, getFakeMail(name), FAKE_PHONE, "Gothenburg", "Sweden", MemberRole.NOT_SET, null);
    }

    /**
     * Get 2 contacts
     *
     * @return 2 contacts
     */
    public static List<ContactPerson> getValidContacts() {
        List<ContactPerson> contacts = new ArrayList<ContactPerson>();
        contacts.add(getValidContactPerson("Joe"));
        contacts.add(getValidContactPerson("Jane"));
        return contacts;
    }

    public static List<ContactPerson> getValidContactsWithoutReferences() {
        List<ContactPerson> contacts = new ArrayList<ContactPerson>();
        contacts.add(getValidContactPersonWithoutReferences("Joe"));
        contacts.add(getValidContactPersonWithoutReferences("Jane"));
        return contacts;
    }

    // -----------------------------------------------------------------------------------------------------------------------
    // ------------------------------ Artist ----------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------

    public static Artist getValidArtist() {
        return getValidArtist(FAKE_BANDNAME, FAKE_CITY, FAKE_COUNTRY);
    }

    /**
     * Get a valid artist to test
     *
     * @param name    name of artist
     * @param city    city of artist
     * @param country country of artist
     * @return fully populated artist
     */
    public static Artist getValidArtist(String name, String city, String country) {
        return new Artist(name, city, country, name + " bio", getUrl(name),
                getUrl(name, "youtube"), getUrl(name, "facebook"), getUrl(name, "spotify"),
                getUrl(name, "iTunes"), getFakeMail(name), FAKE_GENRE);
    }

    /**
     * Same as {@link #getValidArtist()} except that this one
     * creates empty {@link ArrayList}s of all the references. This helps in avoiding
     * {@link NullPointerException}s in unit tests
     *
     * @return {@link #getValidArtist() with empty collection references
     */
    public static Artist getValidArtistWithEmptyReferences() {
        Artist a = getValidArtist();
        a.setMembers(new ArrayList<>());
        a.setVenues(new ArrayList<>());
        a.setRequests(new ArrayList<>());
        a.setGigs(new ArrayList<>());
        a.setReviews(new ArrayList<>());
        return a;
    }

//	/**
//	 * Same as {@link #getValidArtist()} except that this one 
//	 * creates empty {@link ArrayList}s of all the references. This helps in avoiding 
//	 * {@link NullPointerException}s in unit tests
//	 * @return {@link #getValidArtist() with empty collection references
//	 */
//	public static Artist getValidArtistWithEmptyReferences(String name, String city, String country) {
//		Artist a = getValidArtist(name, city, country);
//		a.setMembers(new ArrayList<>());
//		a.setVenues(new ArrayList<>());
//		a.setRequests(new ArrayList<>());
//		a.setGigs(new ArrayList<>());
//		a.setReviews(new ArrayList<>());
//		return a;
//	}

    public static Artist getValidArtist(String name, String city, String country, List<Member> members) {
        Artist validArtist = getValidArtist(name, city, country);
        validArtist.setMembers(members);
        validArtist.setRequests(getValidRequests());
        validArtist.setVenues(getValidVenues());
        validArtist.setReviews(getValidReviews());
        validArtist.setGigs(new ArrayList<Gig>());
        return validArtist;
    }

    public static List<Artist> getValidArtists() {
        List<Artist> artists = new ArrayList<Artist>();
        artists.add(getValidArtist());
        artists.add(getValidArtist("Oasis", "Manchester", "UK"));
        return artists;
    }

    public static List<Artist> getValidArtists(String name1, String name2) {
        List<Artist> artists = new ArrayList<Artist>();
        artists.add(getValidArtist(name1, "London", "Somewhere"));
        artists.add(getValidArtist(name2, "Sabo", "Dabo"));
        return artists;
    }

    // -----------------------------------------------------------------------------------------------------------------------
    // ------------------------------ MEMBER ---------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------

    public static Member getValidMember() {
        return getValidMember(FAKE_USERNAME, FAKE_PASSWORD, FAKE_NAME);
    }

    public static Member getValidMember(String userName, String password, String realName) {
        return new Member(userName, getFakeMail(userName), password, realName, FAKE_CITY, FAKE_COUNTRY, FAKE_ROLE, getValidArtists(), getValidSentMessages(), getValidReceivedMessages());
    }

    public static Member getValidMember(String userName, String password, String realName, String artist1Name, String artist2Name) {
        return new Member(userName, getFakeMail(userName), password, realName, FAKE_CITY, FAKE_COUNTRY, FAKE_ROLE, getValidArtists(artist1Name, artist2Name), getValidSentMessages(), getValidReceivedMessages());
    }

    public static Member getValidMemberWithoutReferences(String userName, String password, String realName) {
        return new Member(userName, getFakeMail(userName), password, realName, FAKE_CITY, FAKE_COUNTRY, FAKE_ROLE);
    }

    public static List<Member> getValidMembers() {
        List<Member> members = new ArrayList<Member>();
        members.add(getValidMember());
        members.add(getValidMember("johanfredin", "@$$h0Lie", "Alex Hedling"));
        return members;
    }

    public static List<Member> getValidMembersWithoutReferences() {
        List<Member> members = new ArrayList<Member>();
        members.add(getValidMemberWithoutReferences(FAKE_USERNAME, FAKE_PASSWORD, FAKE_NAME));
        members.add(getValidMemberWithoutReferences("johanfredin", "@$$h0Lie", "Alex Hedling"));
        return members;
    }


    // -----------------------------------------------------------------------------------------------------------------------
    // ------------------------------ VENUE_REVIEW ---------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------

    public static VenueReview getValidReview() {
        return getValidReview(getValidArtist());
    }

    public static VenueReview getValidReview(Artist artist) {
        return new VenueReview((byte) 10, artist.getName() + " thinks this place is awesome", artist, getValidVenue());
    }

    public static VenueReview getValidReviewWithoutReferences(String artistName) {
        return new VenueReview((byte) 10, artistName + " thinks this place is awesome", null, null);
    }

    public static List<VenueReview> getValidReviews() {
        List<VenueReview> reviews = new ArrayList<VenueReview>();
        reviews.add(getValidReview(getValidArtist("Joe", "Gothenburg", "Sweden")));
        reviews.add(getValidReview(getValidArtist("Jane", "Gothenburg", "Sweden")));
        return reviews;
    }

    public static List<VenueReview> getValidReviewsWithoutReferences() {
        return getValidReviewsWithoutReferences("Taikes", "Nirvana");
    }

    public static List<VenueReview> getValidReviewsWithoutReferences(String bandOne, String bandTwo) {
        List<VenueReview> reviews = new ArrayList<VenueReview>();
        reviews.add(getValidReviewWithoutReferences(bandOne));
        reviews.add(getValidReviewWithoutReferences(bandTwo));
        return reviews;
    }

    // -----------------------------------------------------------------------------------------------------------------------
    // ------------------------------ VENUE ----------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------

    /**
     * @return a new {@link Venue} with all fields filled in with dummy values
     */
    public static Venue getValidVenue() {
        return new Venue(FAKE_VENUE_NAME, getFakeMail(FAKE_VENUE_NAME), getUrl(FAKE_VENUE_NAME), FAKE_PHONE, FAKE_CAPACITY, 1L, FAKE_GENRE,
                getValidReviewsWithoutReferences(), getValidContactsWithoutReferences(), getValidAddress(), getValidArtists());
    }

    public static Venue getValidVenue(String name) {
        return new Venue(name, getFakeMail(name), getUrl(name), FAKE_PHONE, FAKE_CAPACITY, 1L, FAKE_GENRE,
                getValidReviews(), getValidContacts(), getValidAddress(), getValidArtists());
    }

    public static Venue getValidVenueWithoutReferences(String name) {
        return getValidVenueWithoutReferences(name, false);
    }

    /**
     * Get a venue with either null references or initiated empty lists
     *
     * @param name     name of venue
     * @param initRefs if <code>true</code> collection references will be instantiated as empty lists, <code>null</code> otherwise
     * @return a venue
     */
    @SuppressWarnings("unchecked")
    public static Venue getValidVenueWithoutReferences(String name, boolean initRefs) {
        if (initRefs) {
            return new Venue(name, getFakeMail(name), getUrl(name), FAKE_PHONE, FAKE_CAPACITY, 1L, FAKE_GENRE, el(), el(), new Address(), el());
        }
        return new Venue(name, getFakeMail(name), getUrl(name), FAKE_PHONE, FAKE_CAPACITY, 1L, FAKE_GENRE, null, null, null, null);
    }

    @SuppressWarnings("rawtypes")
    public static List el() {
        return new ArrayList<>();
    }

    public static List<Venue> getValidVenues() {
        List<Venue> venues = new ArrayList<Venue>();
        venues.add(getValidVenue(FAKE_VENUE_NAME));
        venues.add(getValidVenue("Sticky"));
        return venues;
    }

    public static List<Venue> getValidVenuesWithoutReferences() {
        List<Venue> venues = new ArrayList<Venue>();
        venues.add(getValidVenueWithoutReferences(FAKE_VENUE_NAME));
        venues.add(getValidVenueWithoutReferences("Sticky"));
        return venues;
    }

    // -----------------------------------------------------------------------------------------------------------------------
    // ------------------------------ GIG REQUEST ----------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------

    public static GigRequest getValidRequest() {
        return getValidRequest(GigRequestStatus.STATUS_APPROVED);
    }

    public static GigRequest getValidRequest(GigRequestStatus status) {
        return new GigRequest(FAKE_DATE, status, getValidVenue(), getValidArtist(), 1L);
    }

    public static GigRequest getValidRequestWithoutReferences(GigRequestStatus status) {
        return new GigRequest(FAKE_DATE, status, null, null, 1L);
    }

    public static List<GigRequest> getValidRequests() {
        List<GigRequest> requests = new ArrayList<GigRequest>();
        requests.add(getValidRequest(GigRequestStatus.STATUS_DENIED));
        requests.add(getValidRequest(GigRequestStatus.STATUS_APPROVED));
        return requests;
    }

    public static List<GigRequest> getValidRequestsWithoutReferences() {
        List<GigRequest> requests = new ArrayList<GigRequest>();
        requests.add(getValidRequestWithoutReferences(GigRequestStatus.STATUS_DENIED));
        requests.add(getValidRequestWithoutReferences(GigRequestStatus.STATUS_APPROVED));
        return requests;
    }


    // -----------------------------------------------------------------------------------------------------------------------
    // ------------------------------ GIG SCHEDULE ---------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------

    public static GigSchedule getValidSchedule() {
        return getValidSchedule(FAKE_TIME);
    }

    /**
     * Gets a new schedule filled in with time variables as following:<br>
     * <code>timeForSoundcheck</code>=18:00<br>
     * <code>openingTime</code>=19:00<br>
     * <code>showupTime</code>=14:00<br>
     * <code>closingTime</code>=02:00<br>
     *
     * @param timeForShow the time to play
     * @return a new {@link GigSchedule} with filled in time values
     */
    public static GigSchedule getValidSchedule(String timeForShow) {
        return new GigSchedule(new Gig(), "14:00", "18:00", "19:00", timeForShow, "02:00");
    }

    public static GigSchedule getValidScheduleWithoutReference() {
        return new GigSchedule(null, "14:00", "18:00", "19:00", "20:00", "02:00");
    }

    // -----------------------------------------------------------------------------------------------------------------------
    // ------------------------------ GIG SPECIFICATIONS ---------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------

    /**
     * Get a new shedule filled with the following:<br>
     * <code>revenue</code>=1000<br>
     * <code>backline</code>=<code>true</code><br>
     * <code>travelCompensation</code>=<code>false</code><br>
     * <code>foodIncluded</code>=<code>true</code><br>
     * <code>freeBeverages</code>=<code>true</code><br>
     * <code>backstage</code>=<code>true</code><br>
     * <code>sidenoter</code>="Bring extra underwear"<br>
     *
     * @return
     */
    public static GigSpecifics getValidSpecifications() {
        return new GigSpecifics(1000, true, false, true, true, true, "Bring extra underwear", getValidGigWithoutReferences());
    }

    public static GigSpecifics getValidSpecificationsWithoutReference() {
        return new GigSpecifics(1000, true, false, true, true, true, "Bring extra underwear", null);
    }

    // -----------------------------------------------------------------------------------------------------------------------
    // ------------------------------ GIG ------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------


    public static Gig getValidGig() {
        return getValidGig(FAKE_BANDNAME);
    }

    public static Gig getValidGig(String bandName) {
        return new Gig(FAKE_DATE, getValidVenue(), getValidArtist(bandName, FAKE_CITY, FAKE_COUNTRY), getValidSchedule(), getValidSpecifications());
    }

    public static Gig getValidGigWithoutReferences() {
        return new Gig(FAKE_DATE, null, null, null, null);
    }

    public static Gig getValidGigWithoutReferences(String date) {
        return new Gig(FAKE_DATE, null, null, null, null);
    }

    public static List<Gig> getValidGigs() {
        List<Gig> gigs = new ArrayList<Gig>();
        gigs.add(getValidGig());
        gigs.add(getValidGig("Oasis"));
        return gigs;
    }

    public static List<Gig> getValidGigsWithoutReferences() {
        List<Gig> gigs = new ArrayList<Gig>();
        gigs.add(getValidGigWithoutReferences());
        gigs.add(getValidGigWithoutReferences("2015-09-26"));
        return gigs;
    }


    // -----------------------------------------------------------------------------------------------------------------------
    // ------------------------------ MESSAGE --------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------

    public static Message getValidMessage() {
        return getValidMessage(FAKE_MESSEGE_TO_VENUE);
    }

    public static Message getValidMessage(String text, MessageSubject subject, boolean createMoreFakeData) {
        if (createMoreFakeData) {
            return getValidMessage(text, subject);
        }
        Message message = new Message(text);
        message.setSubject(subject);
        return message;
    }

    public static Message getValidMessage(String message) {
        return getValidMessage(message, MessageSubject.GIG_REQUEST);
    }

    public static Message getValidMessage(String message, MessageSubject subject) {
        return getValidMessage(message, subject, null, null);
    }

    public static Message getValidMessage(String message, MessageSubject subject, Member sender, Member receiver) {
        return new Message(message, subject, sender, receiver);
    }

    public static List<Message> getValidMessages(MessageType type) {
        switch (type) {
            case SENT:
                return getValidSentMessages();
            case RECEIVED:
                return getValidReceivedMessages();
            default:
                return new ArrayList<Message>();
        }
    }

    public static List<Message> getValidReceivedMessages() {
        List<Message> messages = new ArrayList<Message>();
        messages.add(getValidMessage(FAKE_RESPONSE_FROM_MEMBER));
        messages.add(getValidMessage(FAKE_RESPONSE_FROM_VENUE));
        return messages;
    }

    public static List<Message> getValidSentMessages() {
        List<Message> messages = new ArrayList<Message>();
        messages.add(getValidMessage(FAKE_MESSAGE_TO_MEMBER));
        messages.add(getValidMessage(FAKE_MESSEGE_TO_VENUE));
        return messages;
    }

    // Deployment --------------------------------------------------------------------------------------------------------------
    public static Validator getValidator() {
        Configuration<?> configure = Validation.byDefaultProvider().configure();
        return configure.buildValidatorFactory().getValidator();
    }


}
