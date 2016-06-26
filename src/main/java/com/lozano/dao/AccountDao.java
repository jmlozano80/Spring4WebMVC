package com.lozano.dao;

import com.lozano.model.Account;
import com.lozano.model.Test;

/**
 * Created by jose on 14/05/16.
 */
public interface AccountDao {

    public Account findAccount(Long id);
    public Account findAccountByUsername(String username);
    public Account findAccountByEmail(String email);
    public Account createAccount(Account acc);
    public Account updateAccount(Account acc);
    public Test getTest(Account acc);
    public Test setTestWord(Account acc, String testWord);
}