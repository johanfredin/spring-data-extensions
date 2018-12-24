package com.github.johanfredin.springdataextensions.web.mvc.bean;

import com.github.johanfredin.springdataextensions.constants.Constants;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class LoginMemberBean {

    @NotBlank
    private String userName;

    @Length(min = Constants.MIN_LENGTH_PWD, max = Constants.MAX_LENGTH_PWD)
    private String password;

    public LoginMemberBean() {
    }

    public LoginMemberBean(String userName, String password) {
        setUserName(userName);
        setPassword(password);
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

    @Override
    public String toString() {
        return "LoginMemberBean [userName=" + userName + ", password=" + password + "]";
    }


}
