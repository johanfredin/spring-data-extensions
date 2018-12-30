package com.github.johanfredin.springdataextensions.domain;

import java.io.Serializable;

public interface Identifiable<ID> {

    ID getId();

    void setId(ID id);

    boolean isExistingEntity();
}
