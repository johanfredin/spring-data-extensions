package com.github.johanfredin.springdataextensions.web;

import com.github.johanfredin.springdataextensions.constants.Constants;
import com.github.johanfredin.springdataextensions.domain.Member;
import com.github.johanfredin.springdataextensions.web.mvc.controller.ControllerBase;

/**
 * Utility methods for {@link ControllerBase} implementations
 *
 * @author johan
 */
public class MvcUtils {

    /**
     * @param password       the new password to set
     * @param repeatPassword the new password to set that must match param1
     * @return <code>true</code> if the password is valid but not matching the repeatPassword
     */
    public static boolean isRepeatPasswordIncorrect(String password, String repeatPassword) {
        boolean isRepeatPasswordCorrect = password.equals(repeatPassword);
        return isValidPassword(password) && !isRepeatPasswordCorrect;
    }

    /**
     * Same as {@link #isRepeatPasswordIncorrect(String, String)} but will return <code>false</code> if
     * any of the 2 passwords passed in are empty
     *
     * @param currentPassword   the current password in db
     * @param persistedPassword the password that existsById in db that the current password must match!
     * @return <code>true</code> if currentPassword and persistedPassword are NOT empty and are identical
     */
    public static boolean isCurrentPasswordIncorrect(String currentPassword, String persistedPassword) {
        if (currentPassword.isEmpty() || persistedPassword.isEmpty()) {
            return true;
        }
        return isRepeatPasswordIncorrect(currentPassword, persistedPassword);
    }

    /**
     * Check if the password we are trying to use when logging in for given {@link Member}
     * is correct
     *
     * @param dbMember the member in db
     * @return <code>true</code> if password entered is valid but not matching the password for {@link Member} in db
     * and passed in {@link Member} is not <code>null</code>
     */
    public static boolean isValidButIncorrectPassword(String password, Member dbMember) {
        if (dbMember == null || password.isEmpty()) {
            return false;
        }
        boolean isIncorrectPassword = !password.equals(dbMember.getPassword());
        return isValidPassword(password) && isIncorrectPassword;
    }

    /**
     * @param password password to check if is valid
     * @return <code>true</code> if password >= {@link Constants#MIN_LENGTH_PWD} and <= {@link Constants#MAX_LENGTH_PWD}
     */
    public static boolean isValidPassword(String password) {
        return password.length() >= Constants.MIN_LENGTH_PWD && password.length() <= Constants.MAX_LENGTH_PWD;
    }

    /**
     * Check all passed in conditions and return <code>true</code> if one of them is false
     *
     * @param conditions arbitrary amount of booleans to check
     * @return <code>true</code> if one or more of the conditions are <code>false</code>
     */
    public static boolean isAnyErrors(boolean... conditions) {
        for (int i = 0; i < conditions.length; i++) {
            if (!conditions[i]) {
                return true;
            }
        }
        return false;
    }

    public static String redirectTo(String page) {
        return "redirect:/" + page + ".html";
    }

    public static String redirectTo(String page, RedirectEntity re) {
        return redirectTo(page, new RedirectEntity[]{re});
    }

    public static String redirectTo(String page, RedirectEntity... res) {
        StringBuilder sb = new StringBuilder();
        sb.append("redirect:/").append(page).append('/');
        for (int i = 0; i < res.length; i++) {
            sb.append(res[i].toString());
            if (i < res.length - 1) {
                sb.append('/');
            }
        }
        sb.append(".html");

        return sb.toString();
    }

    public static String directTo(String page, RedirectEntity... res) {
        StringBuilder sb = new StringBuilder();
        sb.append('/').append(page).append('/');
        for (int i = 0; i < res.length; i++) {
            sb.append(res[i].toString());
            if (i < res.length - 1) {
                sb.append('/');
            }
        }
        sb.append(".html");

        return sb.toString();
    }

    public static String redirectTo(String page, AbstractEntity... entities) {
        StringBuilder sb = new StringBuilder();
        sb.append("redirect:/").append(page).append('/');
        for (int i = 0; i < entities.length; i++) {
            sb.append(entities[i].getClass().getSimpleName().toLowerCase() + "=" + entities[i].getId());
            if (i < entities.length - 1) {
                sb.append('/');
            }
        }
        sb.append(".html");
        return sb.toString();
    }

    public static String directTo(String page, AbstractEntity... entities) {
        StringBuilder sb = new StringBuilder();
        sb.append('/').append(page).append('/');
        for (int i = 0; i < entities.length; i++) {
            sb.append(entities[i].getClass().getSimpleName().toLowerCase() + "=" + entities[i].getId());
            if (i < entities.length - 1) {
                sb.append('/');
            }
        }
        sb.append(".html");
        return sb.toString();
    }


}
