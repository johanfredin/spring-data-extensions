package com.github.johanfredin.springdataextensions.constants;

public class Constants {

    /**
     * Used for ease of testing the app. for example password length does not need to be 8 characters, etc
     */
    public static final boolean DEBUG_MODE = true;

    public static final String ENTITY_PATH = "com.pocstage.venuehub.domain";

    // Canonical entity class names
    public static final String CLASS_ABSTRACT_ENTITY = ENTITY_PATH + ".AbstractEntity";
    public static final String CLASS_ARTIST = ENTITY_PATH + ".Artist";
    public static final String CLASS_GIG = ENTITY_PATH + ".Gig";
    public static final String CLASS_GIG_REQUEST = ENTITY_PATH + ".GigRequest";
    public static final String CLASS_GIG_SCHEDULE = ENTITY_PATH + ".GigSchedule";
    public static final String CLASS_GIG_SPECIFICS = ENTITY_PATH + ".GigSpecifics";
    public static final String CLASS_MEMBER = ENTITY_PATH + ".Member";
    public static final String CLASS_ADDRESS = ENTITY_PATH + ".Address";
    public static final String CLASS_CONTACTPERSON = ENTITY_PATH + ".ContactPerson";
    public static final String CLASS_VENUE = ENTITY_PATH + ".Venue";
    public static final String CLASS_VENUE_REVIEW = ENTITY_PATH + ".VenueReview";
    public static final String CLASS_MESSENGER = ENTITY_PATH + ".Messenger";

    // Simple names of repositories
    public static final String REPOSITORY_ADDRESS = "AddressRepository";
    public static final String REPOSITORY_ARTIST = "ArtistRepository";
    public static final String REPOSITORY_CONTACT_PERSON = "ContactPersonRepository";
    public static final String REPOSITORY_GIG = "GigRepository";
    public static final String REPOSITORY_GIG_REQUEST = "GigRequestRepository";
    public static final String REPOSITORY_GIG_SCHEDULE = "GigScheduleRepository";
    public static final String REPOSITORY_GIG_SPECIFICS = "GigSpecificsRepository";
    public static final String REPOSITORY_MEMBER = "MemberRepository";
    public static final String REPOSITORY_VENUE = "VenueRepository";
    public static final String REPOSITORY_VENUE_REVIEW = "VenueReviewRepository";


    // Message summary templates
    public static final String MSG_OFFER_MEMBERSHIP_ARTIST = "Member {m} wants to make you a member of Artist {a}";
    public static final String MSG_REQUEST_MEMBERSHIP_ARTIST = "Member {m} wishes to become a Member of {a}";
    public static final String MSG_APPROVE_MEMBERSHIP_ARTIST = "Member {m} granted you membeship of artist {a}";
    public static final String MSG_DENY_MEMBERSHIP_ARTIST = "Member {m} denied you membeship of artist {a}";

    /**
     * Default latitude value (Cobain memorial)
     */
    public static final double DEFAULT_LATITUDE = -123.8056451d;

    /**
     * Default longitude value (Cobain memorial)
     */
    public static final double DEFAULT_LONGITUDE = 46.984246d;

    /**
     * The pattern we will used for a date, "<b>yyyy-MM-dd</b>"
     */
    public static final String REGEX_DATE_PATTERN = "\\d\\d\\d\\d-\\d\\d-\\d\\d";

    /**
     * The pattern we will used for all the time variables, "<b>HH:mm</b>"
     */
    public static final String REGEX_TIME_PATTERN = "(\\d\\d:\\d\\d|| )";

    /**
     * The regex date that will be persisted (CREATION_DATE, LAST_UPDATE)
     */
    public static final String REGEX_DATE_TIME_PATTERN = REGEX_DATE_PATTERN + " " + REGEX_TIME_PATTERN + ":\\d\\d";

    /**
     * The default max lenght for short varchar fields, like those for names, dates etc
     */
    public static final byte LENGTH_SHORT_FIELD = 30;

    /**
     * Good for emails, urls etc
     */
    public static final byte LENGTH_MEDIUM_FIELD = 127;

    /**
     * For short description/sidenotes fields etc...
     */
    public static final short LENGHT_LONG_FIELD = 255;

    /**
     * For long description fields etc...
     */
    public static final short LENGHT_LONG_LONG_FIELD = 512;

    public static final int MAX_CAPACITY = 1000000;

    /**
     * For very short fields such as time
     */
    public static final byte LENGHT_TIME_FIELD = 10;

    /**
     * The default character length for displaying a short version of the review
     */
    public static final byte DEFAULT_SHORT_REVIEW_LENGTH = 25;

    public static final byte LENGTH_SCORE_FIELD = 10;

    public static final byte MIN_LENGTH_PWD = DEBUG_MODE ? 2 : 8;

    public static final byte MAX_LENGTH_PWD = 30;

    /**
     * Used in queries with a limit param. If passing in this value a limit will not be set
     */
    public static final byte UNLIMITED = -1;

    /**
     * Fetch collections ordered by {@link AbstractEntity#getLastChangeDate()}
     */
    public static final String ORDER_BY_DATE = "LAST_UPDATE desc";

}
