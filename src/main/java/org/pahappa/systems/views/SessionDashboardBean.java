package org.pahappa.systems.views;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.pahappa.systems.models.User;
import org.pahappa.systems.models.UserActivity;
import org.pahappa.systems.services.session.SessionManager;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Named
@SessionScoped
public class SessionDashboardBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private SessionManager sessionManager;

    public User getCurrentUser() {
        return sessionManager.getCurrentUser();
    }

    public boolean isUserLoggedIn() {
        return sessionManager.isUserLoggedIn();
    }

    public boolean hasRole(String role) {
        return sessionManager.hasRole(role);
    }

    public String getLoginTime() {
        if (sessionManager.getCurrentUser() != null && sessionManager.getLoginTime() != null) {
            return sessionManager.getLoginTime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return "Not logged in";
    }

    public String getLastActivity() {
        if (sessionManager.getCurrentUser() != null && sessionManager.getLastActivity() != null) {
            return sessionManager.getLastActivity().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return "Not logged in";
    }

    public String getSessionDuration() {
        if (sessionManager.getCurrentUser() != null && sessionManager.getLoginTime() != null) {
            long minutes = java.time.Duration.between(sessionManager.getLoginTime(), java.time.LocalDateTime.now()).toMinutes();
            long hours = minutes / 60;
            minutes = minutes % 60;
            return String.format("%d hours, %d minutes", hours, minutes);
        }
        return "Not logged in";
    }

    public int getRequestCount() {
        if (sessionManager.getCurrentUser() != null) {
            return sessionManager.getRequestCount();
        }
        return 0;
    }

    public String getIpAddress() {
        if (sessionManager.getCurrentUser() != null) {
            return sessionManager.getIpAddress();
        }
        return "Not logged in";
    }

    public void logActivity(String action, String description, UserActivity.ActivityType activityType) {
        sessionManager.logUserActivity(action, description, activityType);
    }

    // Properties for activity logging form
    private String action;
    private String description;
    private UserActivity.ActivityType activityType;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserActivity.ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(UserActivity.ActivityType activityType) {
        this.activityType = activityType;
    }
}