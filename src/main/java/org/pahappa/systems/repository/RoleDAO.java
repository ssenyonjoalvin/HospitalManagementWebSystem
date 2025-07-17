package org.pahappa.systems.repository;

import org.pahappa.systems.models.Role;
import org.pahappa.systems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class RoleDAO {
    public void save(Role role) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(role);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void update(Role role) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(role);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Role getById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Role.class, id);
        }
    }

    public List<Role> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Role r order by r.id desc", Role.class).list();
        }
    }

    public void delete(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Role role = session.get(Role.class, id);
            if (role != null) session.delete(role);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
} 