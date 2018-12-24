package com.github.johanfredin.springdataextensions.web.mvc.bean;

import com.github.johanfredin.springdataextensions.web.MvcUtils;

/**
 * Same as {@link ResetMemberPasswordBean} but only used with the {@link EditMemberController}
 *
 * @author johan
 */
public class ResetPasswordBean {

    private String currentPassword;
    private String newPassword;
    private String repeatPassword;

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

    public boolean isRepeatPasswordCorrect(String originalPassword) {
        return isPasswordAltered() && MvcUtils.isValidPassword(newPassword) && getCurrentPassword().equals(originalPassword);
    }

    public boolean isPasswordAltered() {
        return getNewPassword() != null && !getNewPassword().isEmpty();
    }

    @Override
    public String toString() {
        return "ResetPasswordBean [newPassword=" + newPassword + ", repeatPassword=" + repeatPassword + "]";
    }


}
