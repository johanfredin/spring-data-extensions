package com.github.johanfredin.springdataextensions.service.impl;

import com.github.johanfredin.springdataextensions.repository.MemberRepository;
import com.github.johanfredin.springdataextensions.repository.custom.DeleteMemberRepository;
import com.github.johanfredin.springdataextensions.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("memberService")
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    private DeleteMemberRepository deleteMemberRepository;

    @Override
    public MemberRepository getRepository() {
        return this.memberRepository;
    }

    @Override
    public void setRepository(MemberRepository repository) {
        this.memberRepository = repository;
    }

    @Override
    public DeleteMemberRepository getDeleteMemberRepository() {
        return this.deleteMemberRepository;
    }

    @Override
    public void setDeleteMemberRepository(DeleteMemberRepository repository) {
        this.deleteMemberRepository = repository;
    }

    @Override
    public String toString() {
        return "MemberServiceImpl [memberRepository=" + memberRepository + ", deleteMemberRepository="
                + deleteMemberRepository + "]";
    }


}
