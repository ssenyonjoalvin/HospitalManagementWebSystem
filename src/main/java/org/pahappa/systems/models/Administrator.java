package org.pahappa.systems.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name= "system_admin")
public class Administrator extends User implements LoginCapable {


    @OneToOne(cascade = jakarta.persistence.CascadeType.ALL)
    @JoinColumn(name = "user_account_id", referencedColumnName = "id")
    private UserAccount userAccount;
    @Override
    public UserAccount getUserAccount() {
        return userAccount;
    }

    @Override
    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public String getPassword() {
        return userAccount != null ? userAccount.getPassword() : null;
    }
    @Override
    public void setPassword(String password) {
        if (userAccount != null) userAccount.setPassword(password);
    }

    @Override
    public String getUsername() {
        return userAccount != null ? userAccount.getUserName() : null;
    }
    @Override
    public void setUsername(String username) {
        if (userAccount != null) userAccount.setUserName(username);
    }
}
