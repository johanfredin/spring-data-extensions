package com.github.johanfredin.exstensionsdemo.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Address implements Identifiable<Long>, Copyable<Long, Address>, CrossReferenceHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String street;
    private String zipCode;
    private String city;
    private String country;

    @OneToOne(cascade = CascadeType.MERGE)
    private Person person;

    public Address() {}

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public boolean isExistingEntity() {
        return getId() > 0;
    }

    @Override
    public void copyFrom(@NotNull Address populatedEntity) {
        setId(populatedEntity.getId());
        setStreet(populatedEntity.getStreet());
        setCity(populatedEntity.getCity());
        setZipCode(populatedEntity.getZipCode());
        setCountry(populatedEntity.getCountry());
        setPerson(populatedEntity.getPerson());
    }

    @Override
    public Address createCopy() {
        Address copy = new Address();
        copy.setId(getId());
        copy.setStreet(getStreet());
        copy.setZipCode(getZipCode());
        copy.setCity(getCity());
        copy.setCountry(getCountry());
        copy.setPerson(getPerson());
        return copy;
    }

    @Override
    public void setCrossRelations() {
        getPerson().setAddress(this);
    }


}
