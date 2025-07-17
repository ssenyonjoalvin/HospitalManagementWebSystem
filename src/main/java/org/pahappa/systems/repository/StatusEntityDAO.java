package org.pahappa.systems.repository;

import org.pahappa.systems.models.StatusEntity;
import org.pahappa.systems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class StatusEntityDAO {
    public void save(StatusEntity entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void update(StatusEntity entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public StatusEntity getById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(StatusEntity.class, id);
        }
    }

    public List<StatusEntity> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from StatusEntity s order by s.id desc", StatusEntity.class).list();
        }
    }
    public StatusEntity getStatusByName(String statusName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM StatusEntity WHERE name = :name", StatusEntity.class)
                          .setParameter("name", statusName)
                          .uniqueResult();
        }
    }

    public void delete(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            StatusEntity entity = session.get(StatusEntity.class, id);
            if (entity != null) session.delete(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
} 