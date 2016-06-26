package com.lozano.model.form;

import javax.validation.constraints.Pattern;

/**
 * Created by jose on 15/05/16.
 */
public class ResendEmailForm {
    @Pattern(regexp = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$", message = "Please enter a valid email format.")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
