package com.github.johanfredin.springdataextensions.web.mvc.bean;

import javax.validation.Valid;

public class EditArtistBean extends Bean<Artist> {

    @Valid
    private Artist artist;

    private long memberId;

    public EditArtistBean(Artist artist) {
        this(artist, 0L);
    }

    public EditArtistBean(Artist artist, long memberId) {
        super(artist);
        setMemberId(memberId);
    }

    @Override
    public Artist getEntity() {
        return this.artist;
    }

    @Override
    public void setEntity(Artist entity) {
        this.artist = entity;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "EditArtistBean [\nartist=" + artist + ", \nmemberId=" + memberId + "]";
    }


}
