package org.pahappa.systems.repository;

import org.pahappa.systems.models.UserActivity;
import org.pahappa.systems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class UserActivityDAO {

    public void saveRecord(UserActivity activity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(activity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<UserActivity> getActivitiesByUser(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM UserActivity WHERE user.id = :userId ORDER BY createdAt DESC",
                            UserActivity.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    public List<UserActivity> getAllActivities() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM UserActivity ORDER BY createdAt DESC", UserActivity.class).list();
        }
    }

    public List<UserActivity> getActivitiesByType(UserActivity.ActivityType activityType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM UserActivity WHERE activityType = :type ORDER BY createdAt DESC",
                            UserActivity.class)
                    .setParameter("type", activityType)
                    .list();
        }
    }
}