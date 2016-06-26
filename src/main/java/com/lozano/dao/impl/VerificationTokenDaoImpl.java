package com.lozano.dao.impl;

import com.lozano.model.Account;
import com.lozano.model.VerificationToken;
import com.lozano.dao.VerificationTokenDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jose on 14/05/16.
 */
@Repository
@Transactional
public class VerificationTokenDaoImpl implements VerificationTokenDao {

    /*@PersistenceContext
    private EntityManager emgr;*/

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public VerificationToken createVerificationToken(VerificationToken token) {
        /*emgr.persist(token);
        emgr.flush();
        return token;*/
        sessionFactory.getCurrentSession()
                .saveOrUpdate(token);
        sessionFactory.getCurrentSession()
                .flush();
        return token;
    }

    @Override
    public VerificationToken findVerificationToken(String verificationToken) {
        /*Query query = emgr.createQuery("SELECT v FROM verification_token v WHERE v.token = :token");
        query.setParameter("token", verificationToken);
        if (!query.getResultList().isEmpty())
            return (VerificationToken) query.getResultList().get(0);
        else
            return null;*/

        VerificationToken verToken = (VerificationToken) sessionFactory.getCurrentSession()
                .createCriteria(VerificationToken.class)
                .add(Restrictions.eq("token", verificationToken))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).uniqueResult();
        return verToken;
    }

    @Override
    public VerificationToken updateVerificationToken(VerificationToken newToken, Account acc) {
        /*Query query = emgr.createQuery("SELECT v FROM verification_token v WHERE v.acc = :account_id");
        query.setParameter("account_id", acc);
        if (!query.getResultList().isEmpty()) {
            VerificationToken tokenToBeUpdated = (VerificationToken) query.getResultList().get(0);
            tokenToBeUpdated.setToken(newToken.getToken());
            tokenToBeUpdated.setExpiryDate(newToken.getExpiryDate());
            return emgr.merge(tokenToBeUpdated);
        } else
            return null;*/

        VerificationToken tokenToBeUpdated = (VerificationToken) sessionFactory.getCurrentSession()
                .createCriteria(VerificationToken.class)
                .add(Restrictions.eq("acc", acc))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).uniqueResult();

        if (tokenToBeUpdated != null) {
            tokenToBeUpdated.setToken(newToken.getToken());
            tokenToBeUpdated.setExpiryDate(newToken.getExpiryDate());
            sessionFactory.getCurrentSession()
                    .saveOrUpdate(tokenToBeUpdated);
            sessionFactory.getCurrentSession()
                    .flush();
        }

        return tokenToBeUpdated;
    }
}
