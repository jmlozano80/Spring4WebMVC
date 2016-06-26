package com.lozano.service.impl;

import com.lozano.service.EmailService;
import com.lozano.service.event.OnRegistrationCompleteEvent;
import com.lozano.service.event.OnResendEmailEvent;
import com.lozano.service.event.OnResetPasswordEvent;
import com.lozano.service.exception.EmailNotSentException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * Created by jose on 15/05/16.
 */
@Service
public class EmailServiceImpl implements EmailService ,ApplicationEventPublisherAware {

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void sendConfirmationEmail(OnRegistrationCompleteEvent onRegistrationCompleteEvent) throws EmailNotSentException {

        eventPublisher.publishEvent(onRegistrationCompleteEvent);
        System.out.println("Published OnRegistrationCompleteEvent");
    }

    @Override
    public void resendConfirmationEmail(OnResendEmailEvent onResendEmailEvent) {
        eventPublisher.publishEvent(onResendEmailEvent);
        System.out.println("Published OnResendEmailEvent");

    }

    @Override
    public void sendResetPasswordEmail(OnResetPasswordEvent onResetPasswordEvent) {
        eventPublisher.publishEvent(onResetPasswordEvent);
        System.out.println("Published onResetPasswordEvent");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        eventPublisher = applicationEventPublisher;
    }
}
