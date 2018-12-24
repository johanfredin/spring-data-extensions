package com.github.johanfredin.springdataextensions.web.mvc.bean;

import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.web.MvcUtils;

import javax.validation.Valid;

public class EditMemberBean extends Bean<Member> {

    @Valid
    private Member member;

    private String currentPassword;
    private String newPassword;
    private String repeatPassword;
    private String newEmail;        // Lets us know if the used changed the members password. If not we don't need to go into db to check it is unique

    public EditMemberBean(Member member) {
        super(member, member.getId());
    }

    @Override
    public Member getEntity() {
        return this.member;
    }

    @Override
    public void setEntity(Member entity) {
        this.member = entity;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setResetPassword(String resetPassword) {
        this.repeatPassword = resetPassword;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public boolean isRepeatPasswordCorrect(String originalPassword) {
        return isPasswordAltered() && MvcUtils.isValidPassword(newPassword) && getCurrentPassword().equals(originalPassword);
    }

    public boolean isPasswordAltered() {
        return getNewPassword() != null && !getNewPassword().isEmpty();
    }

    public boolean isEmailAltered() {
        return getNewEmail() != null && !getNewEmail().isEmpty();
    }

    @Override
    public String toString() {
        return "EditMemberBean [member=" + member + ", currentPassword=" + currentPassword + ", newPassword="
                + newPassword + ", repeatPassword=" + repeatPassword + ", newEmail=" + newEmail
                + ", isPasswordAltered()=" + isPasswordAltered() + ", isEmailAltered()=" + isEmailAltered() + "]";
    }


}
