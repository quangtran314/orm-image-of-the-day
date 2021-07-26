package com.amela.service.impl;

import com.amela.model.Feedback;
import com.amela.service.IFeedbackService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.sql.Date;

@Service
public class HibernateFeedbackServiceImpl implements IFeedbackService {

    private static SessionFactory sessionFactory;
    private static EntityManager entityManager;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.conf.xml")
                    .buildSessionFactory();
            entityManager = sessionFactory.createEntityManager();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Feedback> findAll() {
        String queryStr = "SELECT f FROM Feedback AS f";
        TypedQuery<Feedback> query = entityManager.createQuery(queryStr, Feedback.class);
        return query.getResultList();
    }

    @Override
    public List<Feedback> findAllPerDay(Date date){
        String queryStr = "SELECT f FROM Feedback AS f WHERE f.date = :date";
        TypedQuery<Feedback> query = entityManager.createQuery(queryStr, Feedback.class);
        query.setParameter("date", date);
        return query.getResultList();
    }

    @Override
    public Feedback findOne(Long id) {
        String queryStr = "SELECT f FROM Feedback AS f WHERE f.id = :id";
        TypedQuery<Feedback> query = entityManager.createQuery(queryStr, Feedback.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Feedback save(Feedback feedback) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Feedback origin = new Feedback();
            origin.setRate(feedback.getRate());
            origin.setAuthor(feedback.getAuthor());
            origin.setComment(feedback.getComment());
            origin.setLike(feedback.getLike());
            origin.setDate(feedback.getDate());
            session.saveOrUpdate(origin);
            transaction.commit();
            return origin;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    @Override
    public int likeUpdate(Long id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Feedback origin = findOne(id);
            origin.setLike(origin.getLike() + 1);
            session.saveOrUpdate(origin);
            transaction.commit();
            return origin.getLike();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return 0;
    }
}
