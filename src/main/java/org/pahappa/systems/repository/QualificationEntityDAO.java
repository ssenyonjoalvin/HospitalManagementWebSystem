package org.pahappa.systems.repository;

import org.pahappa.systems.models.QualificationEntity;
import org.pahappa.systems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class QualificationEntityDAO {
    public void save(QualificationEntity entity) {
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

    public void update(QualificationEntity entity) {
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

    public QualificationEntity getById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(QualificationEntity.class, id);
        }
    }

    public List<QualificationEntity> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from QualificationEntity q order by q.id desc", QualificationEntity.class).list();
        }
    }

    public void delete(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            QualificationEntity entity = session.get(QualificationEntity.class, id);
            if (entity != null) session.delete(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
} 