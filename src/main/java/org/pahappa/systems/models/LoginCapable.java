package org.pahappa.systems.models;

public interface LoginCapable {

    UserAccount getUserAccount();
    void setUserAccount(UserAccount userAccount);
    String getPassword();
    void setPassword(String password);
    String getUsername();
    void setUsername(String username);
}