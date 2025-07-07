package org.pahappa.systems.views;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.pahappa.systems.models.User;
import org.pahappa.systems.models.UserActivity;
import org.pahappa.systems.services.session.SessionManager;
import org.pahappa.systems.services.session.SessionManager.SessionInfo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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

    public Map<String, SessionInfo> getActiveSessions() {
        return sessionManager.getActiveSessions();
    }

    public int getActiveSessionCount() {
        return sessionManager.getActiveSessionCount();
    }

    public String getCurrentSessionId() {
        return sessionManager.getCurrentUser() != null ? sessionManager.getActiveSessions().keySet().stream()
                .filter(sessionId -> {
                    SessionInfo info = sessionManager.getActiveSessions().get(sessionId);
                    return info != null && info.getUser().equals(getCurrentUser());
                })
                .findFirst()
                .orElse("Unknown") : "Not logged in";
    }

    public String getLoginTime() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            return sessionManager.getActiveSessions().values().stream()
                    .filter(info -> info.getUser().equals(currentUser))
                    .findFirst()
                    .map(info -> info.getLoginTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .orElse("Unknown");
        }
        return "Not logged in";
    }

    public String getLastActivity() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            return sessionManager.getActiveSessions().values().stream()
                    .filter(info -> info.getUser().equals(currentUser))
                    .findFirst()
                    .map(info -> info.getLastActivity().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .orElse("Unknown");
        }
        return "Not logged in";
    }

    public String getSessionDuration() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            return sessionManager.getActiveSessions().values().stream()
                    .filter(info -> info.getUser().equals(currentUser))
                    .findFirst()
                    .map(info -> {
                        LocalDateTime loginTime = info.getLoginTime();
                        LocalDateTime now = LocalDateTime.now();
                        long hours = java.time.Duration.between(loginTime, now).toHours();
                        long minutes = java.time.Duration.between(loginTime, now).toMinutesPart();
                        return String.format("%d hours, %d minutes", hours, minutes);
                    })
                    .orElse("Unknown");
        }
        return "Not logged in";
    }

    public int getRequestCount() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            return sessionManager.getActiveSessions().values().stream()
                    .filter(info -> info.getUser().equals(currentUser))
                    .findFirst()
                    .map(SessionInfo::getRequestCount)
                    .orElse(0);
        }
        return 0;
    }

    public String getIpAddress() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            return sessionManager.getActiveSessions().values().stream()
                    .filter(info -> info.getUser().equals(currentUser))
                    .findFirst()
                    .map(SessionInfo::getIpAddress)
                    .orElse("Unknown");
        }
        return "Not logged in";
    }

    public void logActivity(String action, String description, UserActivity.ActivityType activityType) {
        sessionManager.logUserActivity(action, description, activityType);
    }

    public void cleanupExpiredSessions() {
        sessionManager.cleanupExpiredSessions();
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