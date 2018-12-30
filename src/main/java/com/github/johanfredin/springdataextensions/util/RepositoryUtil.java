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
package com.github.johanfredin.springdataextensions.util;

import com.github.johanfredin.springdataextensions.domain.Identifiable;
import org.springframework.data.domain.PageRequest;

import java.util.Collection;

/**
 * Helper methods for parameters etc using in repository
 *
 * @author johan
 */
public class RepositoryUtil {

    public static final byte DEFAULT_PAGE_RESULTS = 10;
    public static final byte DEFAULT_PAGE = 0;

    /**
     * @return the first 10 results in the first page
     */
    public static PageRequest getFirstTen() {
        return getTenFromPage(DEFAULT_PAGE);
    }

    /**
     * @param page the page to fetch
     * @return the first 10 results at given page
     */
    public static PageRequest getTenFromPage(int page) {
        return PageRequest.of(page, DEFAULT_PAGE_RESULTS);
    }

    /**
     * Helper method for modifying a parameter that is to be used in a like match in a query such as:<br>
     * 'select v from Venue v where v.name like param'<br>
     * calls {@link #lP(String, LikeQuery, boolean)} with args: p, {@link LikeQuery#END}, <code>true</code>
     *
     * @param p the parameter to use in the query
     * @return <code>p%</code>
     */
    public static String lP(String p) {
        return lP(p, LikeQuery.END, true);
    }

    /**
     * Helper method for modifying a parameter that is to be used in a like match in a query such as:<br>
     * 'select v from Venue v where v.name like param'<br>
     * calls {@link #lP(String, LikeQuery, boolean)} with args: p, {@link LikeQuery#END}, <code>isPreparedStatementParam</code>
     *
     * @param p                        the parameter to use in the query
     * @param isPreparedStatementParam whether or not the parameter is used in a prepared statement
     * @return isPreparedStatementParam ? <code>p%</code>, else <code>'p%'</code>
     */
    public static String lP(String p, boolean isPreparedStatementParam) {
        return lP(p, LikeQuery.END, isPreparedStatementParam);
    }

    /**
     * Helper method for modifying a parameter that is to be used in a like match in a query such as:<br>
     * 'select v from Venue v where v.name like param'<br>
     * calls {@link #lP(String, LikeQuery, boolean)} with args: p, <code>lq</code>, <code>true</code>
     *
     * @param p the parameter to use in the query
     * @return {@link #lP(String, LikeQuery, boolean)} with args: p, {@link LikeQuery#END}, <code>true</code>
     */
    public static String lP(String p, LikeQuery lq) {
        return lP(p, lq, true);
    }

    /**
     * Pass in a param used in a 'like' query
     *
     * @param p                        the parameter to use
     * @param lq                       the {@link LikeQuery} to use
     * @param isPreparedStatementParam whether or not the parameter is used in a prepared statement
     * @return param modified to one of: p%, %p or %p% depending on lq param, param will be wrapped in '' if isPreparedStatmentParam = <code>false</code>
     */
    public static String lP(String p, LikeQuery lq, boolean isPreparedStatementParam) {
        if (p == null || p.isEmpty()) {
            return "";
        }

        switch (lq) {
            case START:
                p = isPreparedStatementParam ? ("%" + p) : ("'%" + p + "'");
                break;
            case END:
                p = isPreparedStatementParam ? (p + "%") : ("'" + p + "%'");
                break;
            case ALL:
                p = isPreparedStatementParam ? ("%" + p + "%") : ("'%" + p + "%'");
                break;
        }
        return p;
    }

    /**
     * Check that the query param is valid
     *
     * @param p the parameter to check
     * @return <code>true</code> if p != null AND if p is a string, not empty
     */
    public static boolean isValidParam(Object p) {
        if (p == null) {
            return false;
        }
        if (p instanceof String) {
            return !((String) p).isEmpty();
        }
        return true;
    }

    public static String getByteArgsAsString(byte... args) {
        StringBuilder sb = new StringBuilder();
        for (byte b = 0; b < args.length; b++) {
            sb.append(b);
        }
        return sb.toString();
    }

    public static String getBooleanArgsAsString(boolean... args) {
        StringBuilder sb = new StringBuilder();
        for (boolean arg : args) {
            sb.append(arg ? "1" : "0");
        }
        return sb.toString();
    }

    /**
     * Utility method for getting a string representation of identifiers in a collection.
     * Useful in the toString() methods to quickly spot child entities etc.
     *
     * @param entities the collection of {@link com.github.johanfredin.springdataextensions.domain.Identifiable} instances we want to see the ids of
     * @return a String representation as [1,2,3] of the ids for passed in entities or [] if entities are empty or NULL when null
     */
    public static String getIdsForEntity(Collection<? extends Identifiable> entities) {
        if (entities != null) {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            int i = 0;
            for (Identifiable entity : entities) {
                sb.append(entity.getId());
                if (i < entities.size() - 1) {
                    sb.append(',');
                }
                i++;
            }
            sb.append(']');
            return sb.toString();
        }
        return "NULL";
    }



}
