package com.lozano.dao;

import com.lozano.model.Account;
import com.lozano.model.Role;
import com.lozano.model.Test;

import java.util.Set;

/**
 * Created by jose on 14/05/16.
 */
public interface AccountDao {

    public Account findAccount(Long id);
    public Account findAccountByUsername(String username);
    public Account findAccountByEmail(String email);
    public String getUserNameByEmail(String email);
    public Account createAccount(Account acc);
    public Account updateAccount(Account acc);
    public Test getTest(Account acc);
    public Test setTestWord(Account acc, String testWord);
    public Set<Role> getUserRoles(String email);
}