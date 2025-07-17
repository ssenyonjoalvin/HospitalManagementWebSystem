package org.pahappa.systems.views;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.pahappa.systems.models.User;
import org.pahappa.systems.navigation.NavigationBean;
import org.pahappa.systems.services.session.SessionManager;
import org.pahappa.systems.services.user.UserService;

import java.io.Serial;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String userName;
    private String password;

    @Inject
    private UserService userService;

    @Inject
    private NavigationBean navigationBean;

    @Inject
    private SessionManager sessionManager;

    public String login() {
        try {
            User user = userService.login(userName, password);

            System.out.println( "am here at login but un able to make it in " +password);
            if (user != null) {
                System.out.println("Login successful for user: " + user.getFullName());
                sessionManager.createSession(user);
                this.password = null;
                return "/dashboard.xhtml?faces-redirect=true";
            } else {
                System.out.println("Login failed - invalid credentials");
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password.", null));
                return null;
            }
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("Multiple accounts found with the same username")) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login error: Duplicate username found. Please contact the administrator.", null));
            } else {
                System.err.println("Login error: " + e.getMessage());
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login error: " + e.getMessage(), null));
            }
            return null;
        }
    }

    public String logout() {
        // Log activity and destroy session
        sessionManager.logUserActivity("LOGOUT", "User logged out",
                org.pahappa.systems.models.UserActivity.ActivityType.LOGOUT);
        sessionManager.destroySession();

        // Clear form data
        this.userName = null;
        this.password = null;

        return "/login.xhtml?faces-redirect=true";
    }

    public User getLoggedInUser() {
        return sessionManager.getCurrentUser();
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
}
