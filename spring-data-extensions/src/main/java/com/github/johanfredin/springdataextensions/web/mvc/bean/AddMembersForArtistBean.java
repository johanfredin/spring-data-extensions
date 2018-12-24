package com.github.johanfredin.springdataextensions.web.mvc.bean;

import com.github.johanfredin.springdataextensions.domain.Member;

import javax.validation.Valid;
import java.util.List;

public class AddMembersForArtistBean extends Bean<Artist> implements RefreshBean {

    @Valid
    private List<Member> results;

    @Valid
    private List<Member> invitedMembers;

    @Valid
    private Artist artist;

    private long memberId;
    private long inviteId;

    private String userNameOrEmail;

    @SuppressWarnings("unused")
    private AddMembersForArtistBean() {
    }

    public AddMembersForArtistBean(long memberId) {
        super(memberId);
    }

    public AddMembersForArtistBean(Artist artist, long memberId) {
        super(memberId);
        setArtist(artist);
    }

    public AddMembersForArtistBean(Artist artist, long memberId, long inviteId) {
        super(artist, memberId);
        setInviteId(inviteId);
    }

    public AddMembersForArtistBean(String userNameOrEmail, List<Member> results, Artist artist, long memberId) {
        super(artist, memberId);
        setUserNameOrEmail(userNameOrEmail);
        setResults(results);
    }

    public AddMembersForArtistBean(String userNameOrEmail, List<Member> results, Artist artist, long memberId, long inviteId) {
        super(artist, memberId);
        setUserNameOrEmail(userNameOrEmail);
        setResults(results);
        setInviteId(inviteId);
    }

    @Override
    public Artist getEntity() {
        return this.artist;
    }

    @Override
    public void setEntity(Artist entity) {
        this.artist = entity;
    }

    public String getUserNameOrEmail() {
        return userNameOrEmail;
    }

    public void setUserNameOrEmail(String userNameOrEmail) {
        this.userNameOrEmail = userNameOrEmail;
    }

    public List<Member> getResults() {
        return results;
    }

    public void setResults(List<Member> results) {
        this.results = results;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void addInvitedMember(Member member) {
        this.invitedMembers.add(member);
    }

    public long getInviteId() {
        return inviteId;
    }

    public void setInviteId(long inviteId) {
        this.inviteId = inviteId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public Member getMember(long id) {
        if (getResults() != null) {
            for (Member member : getResults()) {
                if (member.getId() == id) {
                    return member;
                }
            }
        }
        return null;
    }

    boolean isInvited(Member member) {
        return this.invitedMembers.contains(member);
    }

    @Override
    public boolean isNull() {
        return userNameOrEmail == null || userNameOrEmail.isEmpty();
    }

    @Override
    public boolean isAlive() {
        return userNameOrEmail != null && !userNameOrEmail.isEmpty();
    }

    @Override
    public String toString() {
        return "AddMembersForArtistBean [results=" + results + ", invitedMembers=" + invitedMembers + ", artist="
                + artist + ", memberId=" + memberId + ", inviteId=" + inviteId + ", userNameOrEmail=" + userNameOrEmail
                + ", isNull()=" + isNull() + ", isAlive()=" + isAlive() + "]";
    }


}
