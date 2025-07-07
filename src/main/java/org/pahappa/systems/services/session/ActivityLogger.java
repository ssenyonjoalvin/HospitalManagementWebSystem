package org.pahappa.systems.services.session;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.pahappa.systems.models.User;
import org.pahappa.systems.models.UserActivity;

@ApplicationScoped
public class ActivityLogger {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void logActivity(User user, String action, String description,
            UserActivity.ActivityType activityType, String sessionId,
            String ipAddress, String userAgent, String pageUrl) {
        try {
            UserActivity activity = new UserActivity(user, action, description, activityType);
            activity.setSessionId(sessionId);
            activity.setIpAddress(ipAddress);
            activity.setUserAgent(userAgent);
            activity.setPageUrl(pageUrl);

            entityManager.persist(activity);
            entityManager.flush();
        } catch (Exception e) {
            // Log error but don't break the application
            System.err.println("Failed to log user activity: " + e.getMessage());
        }
    }

    @Transactional
    public void logSimpleActivity(User user, String action, String description,
            UserActivity.ActivityType activityType) {
        logActivity(user, action, description, activityType, null, null, null, null);
    }
}