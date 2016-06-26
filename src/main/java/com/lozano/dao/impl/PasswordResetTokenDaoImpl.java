package com.lozano.dao.impl;

import com.lozano.model.PasswordResetToken;
import com.lozano.dao.PasswordResetTokenDao;
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
public class PasswordResetTokenDaoImpl implements PasswordResetTokenDao {

    /*@PersistenceContext
    private EntityManager emgr;*/

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public PasswordResetToken createPasswordResetToken(PasswordResetToken token) {
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
    public PasswordResetToken findPasswordResetToken(String token) {
      /*  Query query = emgr.createQuery("SELECT v FROM password_reset_token v WHERE v.token = :token");
        query.setParameter("token", token);
        if (!query.getResultList().isEmpty())
            return (PasswordResetToken) query.getResultList().get(0);
        else
            return null;*/
        PasswordResetToken passwordResetToken = (PasswordResetToken) sessionFactory.getCurrentSession()
                .createCriteria(PasswordResetToken.class)
                .add(Restrictions.eq("token", token))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).uniqueResult();
        return passwordResetToken;
    }
}
