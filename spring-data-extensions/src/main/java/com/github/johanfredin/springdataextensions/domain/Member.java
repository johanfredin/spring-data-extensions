package com.github.johanfredin.springdataextensions.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member implements Identifiable<Long>, Copyable<Long, Member>, ChangeDateHolder, CrossReferenceHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String lastChangeDate;

    @Override
    public String getLastChangeDate() {
        return lastChangeDate;
    }

    @Override
    public void setLastChangeDate(String lastChangeDate) {
        this.lastChangeDate = lastChangeDate;
    }

    @Override
    public Member createCopy(Member populatedEntity) {
        return null;
    }

    @Override
    public void setCrossRelations() {

    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public boolean isExistingEntity() {
        return false;
    }
}
