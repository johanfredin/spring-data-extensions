package com.github.johanfredin.springdataextensions.web;

/**
 * Simple way to hold keys and messages for the errors that can exist beyond the standard hibernate validator.
 *
 * @author johan
 */
public class MvcErrors {

    public static final String EMAIL_EXISTS = "isExistingEmail";
    public static final String EMAIL_EXISTS_MSG_KEY = "isExistingEmailMsg";
    public static final String EMAIL_EXISTS_MSG_VAL = "Email already used by other {entity}";
    public static final String USR_NAME_EXISTS = "isExistingUserName";
    public static final String USR_NAME_EXISTS_MSG_KEY = "isExistingUserNameMsg";
    public static final String USR_NAME_EXISTS_MSG_VAL = "Already a member with that username";
    public static final String NEW_PW_INCORRECT = "isNewPasswordIncorrect";
    public static final String NEW_PW_INCORRECT_MSG_KEY = "isNewPasswordIncorrectMsg";
    public static final String NEW_PW_INCORRECT_MSG_VAL = "Passwords don't match!";
    public static final String USR_NOT_FOUND = "isUserNotFound";
    public static final String USR_NOT_FOUND_MSG_KEY = "isUserNotFoundMsg";
    public static final String USR_NOT_FOUND_MSG_VAL = "No user found with that username";
    public static final String NO_USR_WITH_PW = "isIncorrectPassword";
    public static final String NO_USR_WITH_PW_MSG_KEY = "isIncorrectPasswordMsg";
    public static final String NO_USR_WITH_PW_MSG_VAL = "Incorrect password for username {userName}";
    public static final String FIND_USR_PARAM_EMPTY = "emptyParam";
    public static final String FIND_USR_PARAM_EMPTY_MSG_KEY = "emptyParamMsg";
    public static final String FIND_USR_PARAM_EMPTY_MSG_VALUE = "Parameter can not be empty!";
    public static final String FIND_USR_NO_RESULTS = "noResults";
    public static final String FIND_USR_NO_RESULTS_MSG_KEY = "noResultsMsg";
    public static final String FIND_USR_NO_RESULTS_MSG_VAL = "There are no members matching that email or username";


    /**
     * Email can exist on several entities extending {@link EmailHolder}.
     * We need the message to display correctly depending on entity
     *
     * @param e the entity where the email was not unique
     * @return {@link #EMAIL_EXISTS_MSG_VAL} where the {entity} is replaced with a real value
     */
    public static final String getEmailMessage(EmailHolder e) {
        return EMAIL_EXISTS_MSG_VAL.replace("{entity}", e.getClass().getSimpleName());
    }

    public static final String getNoMatchPasswordMessage(String userName) {
        return NO_USR_WITH_PW_MSG_VAL.replace("{userName}", userName);
    }
}
