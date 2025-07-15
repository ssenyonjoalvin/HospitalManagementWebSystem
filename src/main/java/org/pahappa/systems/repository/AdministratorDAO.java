package org.pahappa.systems.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pahappa.systems.models.Administrator;
import org.pahappa.systems.models.UserAccount;
import org.pahappa.systems.util.HibernateUtil;

public class AdministratorDAO {
    public Administrator getByUserAccount(UserAccount userAccount) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Administrator WHERE userAccount = :userAccount", Administrator.class)
                    .setParameter("userAccount", userAccount)
                    .uniqueResult();
        }
    }

    public void saveRecord(Administrator admin) {
        Transaction transaction = null;
        try (Session session = org.pahappa.systems.util.HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(admin);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
