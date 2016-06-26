package com.lozano.service;

import com.lozano.service.event.OnRegistrationCompleteEvent;
import com.lozano.service.event.OnResendEmailEvent;
import com.lozano.service.event.OnResetPasswordEvent;
import com.lozano.service.exception.EmailNotSentException;

/**
 * Created by jose on 15/05/16.
 */
public interface EmailService {
    public void sendConfirmationEmail(OnRegistrationCompleteEvent onRegistrationCompleteEvent) throws EmailNotSentException;

    public void resendConfirmationEmail(OnResendEmailEvent onResendEmailEvent);

    public void sendResetPasswordEmail(OnResetPasswordEvent onResetPasswordEvent);

}
