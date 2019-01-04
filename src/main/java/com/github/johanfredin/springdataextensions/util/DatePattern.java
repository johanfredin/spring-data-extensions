package com.github.johanfredin.springdataextensions.util;

public class DatePattern {

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

    public static final String REGEX_STRING_REPRESENTATION_PATTERN = "yyy-MM-dd HH:mm:ss";
}
