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
@ApplicationScoped
public class SessionManager implements Serializable {

    private static final long serialVersionUID = 1L;

    // Store active sessions
    private final Map<String, SessionInfo> activeSessions = new ConcurrentHashMap<>();

    @Inject
    private UserService userService;

    @Inject
    private ActivityLogger activityLogger;

    public static class SessionInfo {
        private User user;
        private LocalDateTime loginTime;
        private LocalDateTime lastActivity;
        private String ipAddress;
        private String userAgent;
        private int requestCount;

        public SessionInfo(User user, String ipAddress, String userAgent) {
            this.user = user;
            this.loginTime = LocalDateTime.now();
            this.lastActivity = LocalDateTime.now();
            this.ipAddress = ipAddress;
            this.userAgent = userAgent;
            this.requestCount = 0;
        }

        // Getters and Setters
        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public LocalDateTime getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(LocalDateTime loginTime) {
            this.loginTime = loginTime;
        }

        public LocalDateTime getLastActivity() {
            return lastActivity;
        }

        public void setLastActivity(LocalDateTime lastActivity) {
            this.lastActivity = lastActivity;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public int getRequestCount() {
            return requestCount;
        }

        public void setRequestCount(int requestCount) {
            this.requestCount = requestCount;
        }
    }

    public void createSession(User user) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            HttpSession session = request.getSession();

            String sessionId = session.getId();
            String ipAddress = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");

            SessionInfo sessionInfo = new SessionInfo(user, ipAddress, userAgent);
            activeSessions.put(sessionId, sessionInfo);

            // Log login activity
            activityLogger.logActivity(user, "LOGIN",
                    "User logged in from " + ipAddress,
                    UserActivity.ActivityType.LOGIN,
                    sessionId,
                    ipAddress,
                    userAgent,
                    request.getRequestURI());
        }
    }

    public void updateSessionActivity() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            HttpSession session = request.getSession(false);

            if (session != null) {
                String sessionId = session.getId();
                SessionInfo sessionInfo = activeSessions.get(sessionId);

                if (sessionInfo != null) {
                    sessionInfo.setLastActivity(LocalDateTime.now());
                    sessionInfo.setRequestCount(sessionInfo.getRequestCount() + 1);
                }
            }
        }
    }

    public void destroySession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            HttpSession session = request.getSession(false);

            if (session != null) {
                String sessionId = session.getId();
                SessionInfo sessionInfo = activeSessions.remove(sessionId);

                if (sessionInfo != null) {
                    // Log logout activity
                    activityLogger.logActivity(sessionInfo.getUser(), "LOGOUT",
                            "User logged out",
                            UserActivity.ActivityType.LOGOUT,
                            sessionId,
                            sessionInfo.getIpAddress(),
                            sessionInfo.getUserAgent(),
                            request.getRequestURI());
                }

                session.invalidate();
            }
        }
    }

    public User getCurrentUser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            HttpSession session = request.getSession(false);

            if (session != null) {
                String sessionId = session.getId();
                SessionInfo sessionInfo = activeSessions.get(sessionId);
                return sessionInfo != null ? sessionInfo.getUser() : null;
            }
        }
        return null;
    }

    public boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }

    public boolean hasRole(String role) {
        User user = getCurrentUser();
        return user != null && user.getRole().name().equals(role);
    }

    public void logUserActivity(String action, String description, UserActivity.ActivityType activityType) {
        User user = getCurrentUser();
        if (user != null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (facesContext != null) {
                HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
                HttpSession session = request.getSession(false);

                String sessionId = session != null ? session.getId() : null;
                String ipAddress = getClientIpAddress(request);
                String userAgent = request.getHeader("User-Agent");

                activityLogger.logActivity(user, action, description, activityType,
                        sessionId, ipAddress, userAgent, request.getRequestURI());
            }
        }
    }

    public Map<String, SessionInfo> getActiveSessions() {
        return new HashMap<>(activeSessions);
    }

    public int getActiveSessionCount() {
        return activeSessions.size();
    }

    public void cleanupExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        activeSessions.entrySet().removeIf(entry -> {
            SessionInfo sessionInfo = entry.getValue();
            // Remove sessions inactive for more than 30 minutes
            return sessionInfo.getLastActivity().plusMinutes(30).isBefore(now);
        });
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}