package com.github.johanfredin.springdataextensions.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public interface ChangeDateHolder {

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

    /**
     * @return A new date with format "yyyy-MM-dd HH:mm:ss"
     */
    default String getNewDate() {
        return new DateTime().toString(DateTimeFormat.forPattern("yyy-MM-dd HH:mm:ss"));
    }
}
