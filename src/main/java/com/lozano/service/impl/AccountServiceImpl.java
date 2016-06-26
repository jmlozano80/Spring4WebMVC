package com.lozano.service.impl;

import com.lozano.model.Account;
import com.lozano.model.PasswordResetToken;
import com.lozano.model.Test;
import com.lozano.model.VerificationToken;
import com.lozano.dao.AccountDao;
import com.lozano.dao.PasswordResetTokenDao;
import com.lozano.dao.VerificationTokenDao;
import com.lozano.service.AccountService;
import com.lozano.service.exception.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jose on 14/05/16.
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private VerificationTokenDao tokenRepo;

    @Autowired
    private PasswordResetTokenDao passwordResetRepo;


    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public Test getTest(Account acc) {
        return accountDao.getTest(acc);
    }

    @Override
    public Test setTest(Account acc, String testWord) {
        return accountDao.setTestWord(acc, testWord);
    }

    @Override
    public Account createAccount(Account acc) throws EmailExistsException {
        if (accountDao.findAccountByEmail(acc.getEmail()) != null) {
            throw new EmailExistsException("Email already exists.");
        }

        // Hash the password
        acc.setPassword(passwordEncoder.encode(acc.getPassword()));

        return accountDao.createAccount(acc);
    }

    @Override
    public Account updateAccount(Account acc) {
        return accountDao.updateAccount(acc);
    }

    @Override
    public VerificationToken createVerificationToken(Account acc, String token) {
        return tokenRepo.createVerificationToken(new VerificationToken(token, acc));
    }

    @Override
    public VerificationToken findVerificationToken(String token) {
        return tokenRepo.findVerificationToken(token);
    }

    @Override
    public Account findAccount(String email) {
        return accountDao.findAccountByEmail(email);
    }

    @Override
    public VerificationToken updateVerificationToken(VerificationToken newToken, Account acc) {
        return tokenRepo.updateVerificationToken(newToken, acc);
    }

    @Override
    public PasswordResetToken createPasswordResetToken(Account acc, String token) {
        return passwordResetRepo.createPasswordResetToken(new PasswordResetToken(token,acc));
    }

    @Override
    public PasswordResetToken findPasswordResetToken(String token) {
        return passwordResetRepo.findPasswordResetToken(token);
    }

}
