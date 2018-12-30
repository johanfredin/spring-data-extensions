package com.github.johanfredin.exstensionsdemo.domain;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
public class Person implements Identifiable<Long>, Copyable<Long, Person>, ChangeDateHolder, CrossReferenceHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Pattern(regexp = REGEX_DATE_TIME_PATTERN)
    private String lastChangeDate;

    private String name;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Address address;

    public Person() {}

    @Override
    public String getLastChangeDate() {
        return lastChangeDate;
    }

    @Override
    public void setLastChangeDate(String lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public void copyFrom(Person populatedEntity) {
        setId(populatedEntity.getId());
        setLastChangeDate(populatedEntity.getLastChangeDate());
        setName(populatedEntity.getName());
        setAddress(populatedEntity.getAddress());
    }

    @Override
    public Person createCopy() {
        Person copy = new Person();
        copy.setId(getId());
        copy.setLastChangeDate(getLastChangeDate());
        copy.setName(getName());
        copy.setAddress(getAddress());
        return copy;
    }

    @Override
    public void setCrossRelations() {
        getAddress().setPerson(this);
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public boolean isExistingEntity() {
        return getId() > 0L;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
