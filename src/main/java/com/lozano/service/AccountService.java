package com.lozano.service;

import com.lozano.model.Account;
import com.lozano.model.PasswordResetToken;
import com.lozano.model.Test;
import com.lozano.model.VerificationToken;
import com.lozano.service.exception.EmailExistsException;

/**
 * Created by jose on 14/05/16.
 */
public interface AccountService {
    public Account createAccount(Account acc) throws EmailExistsException;
    public Account updateAccount(Account acc);
    public VerificationToken createVerificationToken(Account acc, String token);
    public VerificationToken findVerificationToken(String token);
    public Account findAccount(String email);
    public VerificationToken updateVerificationToken(VerificationToken newToken, Account acc);
    public PasswordResetToken createPasswordResetToken(Account acc, String token);
    public PasswordResetToken findPasswordResetToken(String token);
    public String encodePassword(String password);
    public Test getTest(Account acc);
    public Test setTest(Account acc, String testWord);
}

