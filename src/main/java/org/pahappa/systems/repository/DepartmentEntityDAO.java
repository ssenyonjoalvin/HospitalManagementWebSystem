package org.pahappa.systems.repository;

import org.pahappa.systems.models.DepartmentEntity;
import org.pahappa.systems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class DepartmentEntityDAO {
    public void save(DepartmentEntity entity) {
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

    public void update(DepartmentEntity entity) {
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

    public DepartmentEntity getById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(DepartmentEntity.class, id);
        }
    }

    public List<DepartmentEntity> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from DepartmentEntity d order by d.id desc", DepartmentEntity.class).list();
        }
    }

    public void delete(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            DepartmentEntity entity = session.get(DepartmentEntity.class, id);
            if (entity != null) session.delete(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
} 