package org.pahappa.systems.views;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import org.pahappa.systems.models.User;
import org.pahappa.systems.models.UserAccount;
import org.pahappa.systems.services.session.SessionManager;
import org.pahappa.systems.services.user.UserService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import org.pahappa.systems.models.LoginCapable;

@Named("profileBean")
@SessionScoped
public class ProfileBean implements Serializable {
    @Inject
    private SessionManager sessionManager;

    @Inject
    private UserService userService;

    private User currentUser;
    private UserAccount currentUserAccount;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
    private boolean changingPassword;
    private LoginCapable loginCapableUser;

    @PostConstruct
    public void init() {
        currentUser = sessionManager.getCurrentUser();
        if (currentUser instanceof LoginCapable) {
            loginCapableUser = (LoginCapable) currentUser;
            currentUserAccount = loginCapableUser.getUserAccount();
        } else {
            loginCapableUser = null;
            currentUserAccount = null;
        }
        changingPassword = false;
        currentPassword = null;
        newPassword = null;
        confirmPassword = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public UserAccount getCurrentUserAccount() {
        return currentUserAccount;
    }

    public void setCurrentUserAccount(UserAccount currentUserAccount) {
        this.currentUserAccount = currentUserAccount;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isChangingPassword() {
        return changingPassword;
    }

    public void setChangingPassword(boolean changingPassword) {
        this.changingPassword = changingPassword;
    }

    public void toggleChangePassword() {
        if (loginCapableUser == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password change not available for this user type.",
                            null));
            return;
        }
        changingPassword = !changingPassword;
        if (!changingPassword) {
            currentPassword = null;
            newPassword = null;
            confirmPassword = null;
        }
    }

    public String updateProfile() {
        try {
            // Password change logic
            if (changingPassword && loginCapableUser != null) {
                if (currentPassword == null || !loginCapableUser.getPassword().equals(currentPassword)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Current password is incorrect.", null));
                    return null;
                }
                if (newPassword == null || !newPassword.equals(confirmPassword)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "New password and confirmation do not match.", null));
                    return null;
                }
                loginCapableUser.setPassword(newPassword); // TODO: Hash password in production
            }
            userService.updateEmployee(currentUser);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile updated successfully!", null));
            changingPassword = false;
            return "profile.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update profile.", null));
            return null;
        }
    }
}
