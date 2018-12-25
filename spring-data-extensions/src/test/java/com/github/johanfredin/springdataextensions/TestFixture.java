package com.github.johanfredin.springdataextensions;

import com.github.johanfredin.springdataextensions.domain.Member;
import com.pocstage.venuehub.constants.*;
import com.pocstage.venuehub.domain.*;

import java.util.ArrayList;
import java.util.List;

public class TestFixture {

    // -----------------------------------------------------------------------------------------------------------------------
    // ------------------------------ MEMBER ---------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------

    public static Member getValidMember() {
        return getValidMember(FAKE_USERNAME, FAKE_PASSWORD, FAKE_NAME);
    }

    public static Member getValidMember(String userName, String password, String realName) {
        return new Member(userName, getFakeMail(userName), password, realName, FAKE_CITY, FAKE_COUNTRY, FAKE_ROLE, getValidArtists(), getValidSentMessages(), getValidReceivedMessages());
    }

    public static Member getValidMember(String userName, String password, String realName, String artist1Name, String artist2Name) {
        return new Member(userName, getFakeMail(userName), password, realName, FAKE_CITY, FAKE_COUNTRY, FAKE_ROLE, getValidArtists(artist1Name, artist2Name), getValidSentMessages(), getValidReceivedMessages());
    }

    public static Member getValidMemberWithoutReferences(String userName, String password, String realName) {
        return new Member(userName, getFakeMail(userName), password, realName, FAKE_CITY, FAKE_COUNTRY, FAKE_ROLE);
    }

    public static List<Member> getValidMembers() {
        List<Member> members = new ArrayList<Member>();
        members.add(getValidMember());
        members.add(getValidMember("johanfredin", "@$$h0Lie", "Alex Hedling"));
        return members;
    }

    public static List<Member> getValidMembersWithoutReferences() {
        List<Member> members = new ArrayList<Member>();
        members.add(getValidMemberWithoutReferences(FAKE_USERNAME, FAKE_PASSWORD, FAKE_NAME));
        members.add(getValidMemberWithoutReferences("johanfredin", "@$$h0Lie", "Alex Hedling"));
        return members;
    }


}
