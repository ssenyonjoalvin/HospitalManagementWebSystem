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
    private String email;
    private String password;

    @Inject
    private UserService userService;

    @Inject
    private NavigationBean navigationBean;

    @Inject
    private SessionManager sessionManager;

    public String login() {
        try {
            //System.out.println("Login attempt for email: " + email);

            User user = userService.login(email, password);
            if (user != null) {
                System.out.println("Login successful for user: " + user.getFullName());

                // Create session and log activity
                sessionManager.createSession(user);

                // Clear sensitive data
                this.password = null;

            
                //System.out.println("Navigation outcome: " + outcome);
                return "/dashboard.xhtml?faces-redirect=true";
            } else {
                System.out.println("Login failed - invalid credentials");
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email or password.", null));
                return null; // Stay on login page
            }
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login error: " + e.getMessage(), null));
            return null;
        }
    }

    public String logout() {
        // Log activity and destroy session
        sessionManager.logUserActivity("LOGOUT", "User logged out",
                org.pahappa.systems.models.UserActivity.ActivityType.LOGOUT);
        sessionManager.destroySession();

        // Clear form data
        this.email = null;
        this.password = null;

        return "/login.xhtml?faces-redirect=true";
    }

    public User getLoggedInUser() {
        return sessionManager.getCurrentUser();
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
