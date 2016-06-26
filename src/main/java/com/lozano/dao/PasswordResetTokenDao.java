package com.lozano.dao;

import com.lozano.model.PasswordResetToken;

/**
 * Created by jose on 14/05/16.
 */
public interface PasswordResetTokenDao {
    public PasswordResetToken createPasswordResetToken(PasswordResetToken token);
    public PasswordResetToken findPasswordResetToken(String token);
}