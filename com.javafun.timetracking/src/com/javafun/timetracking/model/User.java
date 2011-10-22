package com.javafun.timetracking.model;

public class User {

    private String _id;
    private String _email;
    private String _userName;
    private String _pswrd;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getPswrd() {
        return _pswrd;
    }

    public void setPswrd(String pswrd) {
        _pswrd = pswrd;
    }

    public User(String userName, String pswrd) {
        _userName = userName;
        _pswrd = pswrd;
    }

    public String getUserName() {
        return _userName;
    }

    public void setUserName(String userName) {
        _userName = userName;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
    }
}
