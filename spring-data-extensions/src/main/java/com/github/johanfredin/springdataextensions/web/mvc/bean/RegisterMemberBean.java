package com.github.johanfredin.springdataextensions.web.mvc.bean;

import com.github.johanfredin.springdataextensions.domain.Member;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;

public class RegisterMemberBean extends Bean<Member> {

    @Valid
    private Member member;

    @NotBlank
    private String comparePassword;

    public RegisterMemberBean() {
    }

    public RegisterMemberBean(Member member) {
        this(member, null);
    }

    public RegisterMemberBean(Member member, String comparePassword) {
        setEntity(member);
        setComparePassword(comparePassword);
    }

    public String getComparePassword() {
        return comparePassword;
    }

    public void setComparePassword(String comparePassword) {
        this.comparePassword = comparePassword;
    }

    /**
     * If the member is not initialized set artist as default role for the view
     *
     * @return default role
     */
    public MemberRole getRole() {
        return (getMember() == null || getMember().getRole() == null) ? MemberRole.ARTIST : getMember().getRole();
    }

    @Override
    public String toString() {
        return "RegisterMemberBean [member=" + member + ", comparePassword=" + comparePassword + "]";
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public Member getEntity() {
        return this.member;
    }

    @Override
    public void setEntity(Member entity) {
        this.member = entity;
    }


}
