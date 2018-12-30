package com.github.johanfredin.springdataextensions.domain;

public interface Identifiable<ID> {

    ID getId();

    void setId(ID id);

    boolean isExistingEntity();
}
