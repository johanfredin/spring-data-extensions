package com.github.johanfredin.springdataextensions.repository.mock;

import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class MockMemberRepository extends MockRepository<Member> implements MemberRepository {

    @Override
    public boolean isEmailUnique(String email) {
        for (Member m : findAll()) {
            if (m.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isNoOtherEntityWithEmail(long id, String email) {
        for (Member m : findAll()) {
            if (m.getEmail().equals(email) && m.getId() != id) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Member> findAllByArtistsIn(Artist artist) {
        List<Member> byArtist = new ArrayList<Member>();
        for (Member m : findAll()) {
            if (m.getArtists().contains(artist)) {
                byArtist.add(m);
            }
        }
        return byArtist;
    }

    @Override
    public Member findOneByUserName(String userName) {
        for (Member m : findAll()) {
            if (m.getUserName().equals(userName)) {
                return m;
            }
        }
        return null;
    }

    @Override
    public Member findOneByUserNameAndPassword(String userName, String password) {
        for (Member m : findAll()) {
            if (m.getUserName().equals(userName) && m.getPassword().equals(password)) {
                return m;
            }
        }
        return null;
    }

    @Override
    public List<Member> findAllByUserNameContaining(String userName) {
        List<Member> byUserName = new ArrayList<>();
        for (Member m : findAll()) {
            if (m.getUserName().contains(userName)) {
                byUserName.add(m);
            }
        }
        return byUserName;
    }

    @Override
    public List<Member> findAllByEmailIsLike(String email) {
        List<Member> byEmail = new ArrayList<>();
        for (Member m : findAll()) {
            if (m.getEmail().contains(email)) {
                byEmail.add(m);
            }
        }
        return byEmail;
    }

    @Override
    public Page<Member> findAllByUserNameIsLike(String userName, Pageable p) {
        return null;
    }

    @Override
    public boolean isUserNameUnique(String userName) {
        for (Member m : findAll()) {
            if (m.getUserName().equals(userName)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isNoOtherEntityWithUserName(long memberId, String userName) {
        for (Member m : findAll()) {
            if (m.getUserName().equals(userName) && m.getId() != memberId) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Page<Member> findAllByEmailIsLike(String email, Pageable p) {
        return null;
    }

    @Override
    public Member getMemberWithAllChildren(long memberId) {
        return getOne(memberId);
    }

    @Override
    public Member getMemberWithArtists(long memberId) {
        return getOne(memberId);
    }

    @Override
    public Member getMemberWithMessages(long memberId) {
        return getOne(memberId);
    }

}
