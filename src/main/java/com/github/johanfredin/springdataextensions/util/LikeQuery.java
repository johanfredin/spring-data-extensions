package com.github.johanfredin.springdataextensions.util;

/**
 * How to match a 'like' query
 *
 * @author johan
 */
public enum LikeQuery {

    /**
     * param = 'param%'
     */
    START,
    /**
     * param = '%param'
     */
    END,
    /**
     * param = '%param%'
     */
    ALL

}
