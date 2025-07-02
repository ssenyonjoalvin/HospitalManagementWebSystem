package org.pahappa.systems.beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.pahappa.systems.models.User;
import org.pahappa.systems.services.UserService;

import java.io.Serial;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String email;
    private String password;
    private User loggedInUser;

    @Inject
    private UserService userService;

    @Inject
    private NavigationBean navigationBean;

    public String login() {
        User user = userService.login(email, password);
        if (user != null) {
            this.loggedInUser = user;
            // Use NavigationBean for navigation
            return navigationBean.toDashboard();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email or password.", null));
            return null; // Stay on login page
        }
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
