package org.pahappa.systems.services.session;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.pahappa.systems.models.User;
import org.pahappa.systems.models.UserActivity;
import org.pahappa.systems.services.user.UserService;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Named
@SessionScoped
public class SessionManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private User currentUser;
    private LocalDateTime loginTime;
    private LocalDateTime lastActivity;
    private String ipAddress;
    private String userAgent;
    private int requestCount;

    @Inject
    private UserService userService;

    @Inject
    private ActivityLogger activityLogger;

    public void createSession(User user) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            HttpSession session = request.getSession();

            this.currentUser = user;
            this.loginTime = LocalDateTime.now();
            this.lastActivity = LocalDateTime.now();
            this.ipAddress = getClientIpAddress(request);
            this.userAgent = request.getHeader("User-Agent");
            this.requestCount = 0;

            // Log login activity
            activityLogger.logActivity(user, "LOGIN",
                    "User logged in from " + ipAddress,
                    UserActivity.ActivityType.LOGIN,
                    session.getId(),
                    ipAddress,
                    userAgent,
                    request.getRequestURI());
        }
    }

    public void updateSessionActivity() {
        this.lastActivity = LocalDateTime.now();
        this.requestCount++;
    }

    public void destroySession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            HttpSession session = request.getSession(false);

            if (session != null) {
                // Log logout activity
                activityLogger.logActivity(currentUser, "LOGOUT",
                        "User logged out",
                        UserActivity.ActivityType.LOGOUT,
                        session.getId(),
                        ipAddress,
                        userAgent,
                        request.getRequestURI());
                session.invalidate();
            }
        }
        this.currentUser = null;
        this.loginTime = null;
        this.lastActivity = null;
        this.ipAddress = null;
        this.userAgent = null;
        this.requestCount = 0;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public boolean hasRole(String role) {
        return currentUser != null && currentUser.getRole().name().equals(role);
    }

    public void logUserActivity(String action, String description, UserActivity.ActivityType activityType) {
        if (currentUser != null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (facesContext != null) {
                HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
                HttpSession session = request.getSession(false);
                activityLogger.logActivity(currentUser, action, description, activityType,
                        session != null ? session.getId() : null,
                        ipAddress,
                        userAgent,
                        request.getRequestURI());
            }
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String getCurrentUserRole() {
        User user = getCurrentUser();
        return user != null && user.getRole() != null ? user.getRole().name() : null;
    }
    public org.pahappa.systems.enums.Rolename getCurrentUserRoleEnum() {
        User user = getCurrentUser();
        return user != null ? user.getRole() : null;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }
    public LocalDateTime getLastActivity() {
        return lastActivity;
    }
    public String getIpAddress() {
        return ipAddress;
    }
    public int getRequestCount() {
        return requestCount;
    }
}