package com.lozano.model.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by jose on 15/05/16.
 */
public class RegistrationForm {

    @NotNull
    @Pattern(regexp = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$")
    private String email;

    @Size(min = 5, max = 120, message = "{Size.password}")
    @NotNull
    private String password;

    @Size(min = 5, max = 120, message = "{Size.confirmPassword}")
    @NotNull
    private String confirmPassword;

    @Size(min = 5, max = 120, message = "{Size.confirmPassword}")
    @NotNull
    private String userName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
