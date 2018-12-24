package com.github.johanfredin.springdataextensions.web.mvc.bean;

public interface RefreshBean {

    default boolean isNull() {
        return this == null;
    }

    boolean isAlive();

}
