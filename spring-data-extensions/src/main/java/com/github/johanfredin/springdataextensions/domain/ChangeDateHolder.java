package com.github.johanfredin.springdataextensions.domain;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public interface ChangeDateHolder {

    /**
     * The pattern we will used for a date, "<b>yyyy-MM-dd</b>"
     */
    String REGEX_DATE_PATTERN = "\\d\\d\\d\\d-\\d\\d-\\d\\d";

    /**
     * The pattern we will used for all the time variables, "<b>HH:mm</b>"
     */
    String REGEX_TIME_PATTERN = "(\\d\\d:\\d\\d|| )";

    /**
     * The regex date that will be persisted (CREATION_DATE, LAST_UPDATE)
     */
    String REGEX_DATE_TIME_PATTERN = REGEX_DATE_PATTERN + " " + REGEX_TIME_PATTERN + ":\\d\\d";

    /**
     * @return the date when the last change occurred formatted as "yyyy-MM-dd HH:mm:ss"
     */
    String getLastChangeDate();

    /**
     * Set the date when the last change occurred.
     *
     * @param lastChangeDate must be formatted as "yyyy-MM-dd HH:mm:ss"
     */
    void setLastChangeDate(String lastChangeDate);

    /**
     * Set lastChangeDate instance to new date calling {@link #getNewDate()}
     */
    default void updateLastChangeDate() {
        setLastChangeDate(getNewDate());
    }

    /**
     * {@link #getLastChangeDate()} without displaying seconds
     *
     * @return
     */
    default String getDisplayLastChangeDate() {
        return getLastChangeDate().substring(0, getLastChangeDate().lastIndexOf(':'));
    }

    default void setNewDate() {
        setLastChangeDate(getNewDate());
    }

    /**
     * @return A new date with format "yyyy-MM-dd HH:mm:ss"
     */
    default String getNewDate() {
        return new DateTime().toString(DateTimeFormat.forPattern("yyy-MM-dd HH:mm:ss"));
    }

}
