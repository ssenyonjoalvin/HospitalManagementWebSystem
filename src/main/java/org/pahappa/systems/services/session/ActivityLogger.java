package org.pahappa.systems.services.session;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.pahappa.systems.models.User;
import org.pahappa.systems.models.UserActivity;
import org.pahappa.systems.repository.UserActivityDAO;

@ApplicationScoped
public class ActivityLogger {

    @Inject
    private UserActivityDAO userActivityDAO;

    public void logActivity(User user, String action, String description,
            UserActivity.ActivityType activityType, String sessionId,
            String ipAddress, String userAgent, String pageUrl) {
        try {
            UserActivity activity = new UserActivity(user, action, description, activityType);
            activity.setSessionId(sessionId);
            activity.setIpAddress(ipAddress);
            activity.setUserAgent(userAgent);
            activity.setPageUrl(pageUrl);

            userActivityDAO.saveRecord(activity);
        } catch (Exception e) {
            // Log error but don't break the application
            System.err.println("Failed to log user activity: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void logSimpleActivity(User user, String action, String description,
            UserActivity.ActivityType activityType) {
        logActivity(user, action, description, activityType, null, null, null, null);
    }
}