package com.github.johanfredin.springdataextensions.web.mvc.bean;


import com.github.johanfredin.springdataextensions.constants.Constants;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class ResetMemberPasswordBean {

    @NotBlank
    @Length(max = Constants.LENGTH_SHORT_FIELD)
    private String userName;

    @Length(min = Constants.MIN_LENGTH_PWD, max = Constants.MAX_LENGTH_PWD)
    private String password;

    @NotBlank
    private String repeatPassword;

    public ResetMemberPasswordBean() {
    }

    public ResetMemberPasswordBean(String userName, String password, String repeatPassword) {
        setUserName(userName);
        setPassword(password);
        setRepeatPassword(repeatPassword);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    @Override
    public String toString() {
        return "ResetMemberPasswordBean [userName=" + userName + ", password=" + password + ", repeatPassword="
                + repeatPassword + "]";
    }


}
