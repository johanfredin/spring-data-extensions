package com.github.johanfredin.springdataextensions.web;

public class ExtError {

    private boolean state;
    private String msg;

    public ExtError(boolean state, String msg) {
        setState(state);
        setMsg(msg);
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
