package com.github.johanfredin.springdataextensions.web;

public class RedirectEntity {

    private String name;
    private long id;

    public RedirectEntity(String name, long id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name + "=" + id;
    }

}
