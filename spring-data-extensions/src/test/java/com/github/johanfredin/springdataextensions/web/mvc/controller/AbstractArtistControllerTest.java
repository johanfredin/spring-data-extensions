package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.repository.ArtistRepository;
import com.github.johanfredin.springdataextensions.repository.MemberRepository;
import com.github.johanfredin.springdataextensions.repository.custom.DeleteMemberRepository;
import com.github.johanfredin.springdataextensions.service.ArtistService;
import com.github.johanfredin.springdataextensions.service.MemberService;
import com.github.johanfredin.springdataextensions.service.impl.ArtistServiceImpl;
import com.github.johanfredin.springdataextensions.service.impl.MemberServiceImpl;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application_test.properties")
@WebAppConfiguration
@DataJpaTest
public abstract class AbstractArtistControllerTest<C extends ControllerBase<ArtistService>> extends AbstractControllerTest<Artist, ArtistRepository, ArtistService, C> {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected DeleteMemberRepository deleteMemberRepository;

    @Autowired
    protected ArtistRepository artistRepository;

    protected MemberService memberService;
    protected Member member;

    @Before
    public void init() {
        super.init();
        this.memberService = new MemberServiceImpl();
        this.member = TestFixture.getValidMemberWithoutReferences("Luke", "Skywalker", "Lucky Luke");
        this.memberService.setRepository(memberRepository);
        this.memberService.setDeleteMemberRepository(deleteMemberRepository);
    }

    @Override
    protected List<Artist> initTestEntities() {
        this.testEntities = TestFixture.getValidArtists();
        return this.testEntities;
    }

    public MemberRepository getMemberRepository() {
        return memberRepository;
    }

    public MemberService getMemberService() {
        return this.memberService;
    }

    @Override
    public ArtistRepository getRepository() {
        return this.artistRepository;
    }

    @Override
    public ArtistService initService() {
        return new ArtistServiceImpl();
    }

    public Member getMember() {
        return member;
    }

    protected List<Artist> getInitialList(Artist... initialArtists) {
        List<Artist> artists = new ArrayList<>();
        for (Artist a : initialArtists) {
            artists.add(a);
        }
        return artists;
    }

    protected List<Member> getInitialMembers(Member... initialMembers) {
        List<Member> members = new ArrayList<>();
        for (Member m : initialMembers) {
            members.add(m);
        }
        return members;
    }

}
