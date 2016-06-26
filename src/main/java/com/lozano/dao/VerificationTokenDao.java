package com.lozano.dao;

import com.lozano.model.Account;
import com.lozano.model.VerificationToken;

/**
 * Created by jose on 14/05/16.
 */
public interface VerificationTokenDao {
    public VerificationToken createVerificationToken(VerificationToken token);
    public VerificationToken findVerificationToken(String verificationToken);
    public VerificationToken updateVerificationToken(VerificationToken token, Account acc);
}
