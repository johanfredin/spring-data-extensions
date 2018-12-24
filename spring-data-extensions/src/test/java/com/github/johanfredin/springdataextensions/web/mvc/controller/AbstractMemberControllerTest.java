package com.github.johanfredin.springdataextensions.web.mvc.controller;

import com.github.johanfredin.springdataextensions.TestFixture;
import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.repository.MemberRepository;
import com.github.johanfredin.springdataextensions.service.MemberService;
import com.github.johanfredin.springdataextensions.service.impl.MemberServiceImpl;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application_test.properties")
@WebAppConfiguration
@DataJpaTest
@ComponentScan
public abstract class AbstractMemberControllerTest<C extends ControllerBase<MemberService>> extends AbstractControllerTest<Member, MemberRepository, MemberService, C> {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    protected List<Member> initTestEntities() {
        return initTestEntities(
                TestFixture.getValidMember("zolost", "lizardKING", "Johan", "Taikes", "Nirvana"),
                TestFixture.getValidMember("bass", "hetgurka", "Alex", "Taikes", "Aroma"));
    }

    @Override
    public MemberService initService() {
        return new MemberServiceImpl();
    }

    @Override
    public MemberRepository getRepository() {
        return this.memberRepository;
    }

}
