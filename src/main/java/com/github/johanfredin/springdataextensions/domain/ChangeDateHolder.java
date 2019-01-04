/*
 * Copyright 2018 Johan Fredin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.johanfredin.springdataextensions.domain;

import com.github.johanfredin.springdataextensions.util.DatePattern;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Interface for entities that should hold a change date.
 */
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
     * @return A new date with format={@link DatePattern#REGEX_STRING_REPRESENTATION_PATTERN}
     */
    default String getNewDate() {
        return new DateTime().toString(DateTimeFormat.forPattern(DatePattern.REGEX_STRING_REPRESENTATION_PATTERN));
    }

}
