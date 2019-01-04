package com.github.johanfredin.springdataextensions.api.doman;

import com.github.johanfredin.springdataextensions.domain.Identifiable;

import javax.persistence.*;

@Entity
public class Pet implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    public Pet() {}

    public Pet(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Pet(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long aLong) {
        this.id = aLong;
    }

    @Override
    public boolean isPersistedEntity() {
        return id > 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
