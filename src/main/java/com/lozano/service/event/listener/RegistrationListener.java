package com.lozano.service.event.listener;

import com.lozano.model.Account;
import com.lozano.model.VerificationToken;
import com.lozano.service.AccountService;
import com.lozano.service.event.OnRegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

/**
 * Created by jose on 14/05/16.
 */
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        System.out.println("Registration Listener - onApplicationEvent");
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        try {
            Account acc = event.getAccount();
            String token = UUID.randomUUID().toString(); // UUID - A class that represents an immutable universally unique identifier (UUID). A UUID represents a 128-bit value.
            VerificationToken createdToken = accountService.createVerificationToken(acc, token);


            System.out.println("VerificationToken " + ((createdToken == null) ? "not created" : "created and id is: " + createdToken.getId()));


            String recipientEmail = acc.getEmail();
            String subject = messageSource.getMessage("registration.email.subject", null, event.getLocale());
            String confirmationURL = "http://localhost:63342/angularRegistrationRest/index.html#/test/" + token;

            String msg = messageSource.getMessage("registration.email.message", null, event.getLocale());
            System.out.println("The email message is: " + msg);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            mimeMessage.setSubject(subject, "UTF-8");
            mimeMessage.setText(msg + " " + confirmationURL, "UTF-8");

            System.out.println("Sending mail to " + recipientEmail);
            // Send the mail
            mailSender.send(mimeMessage);
        } catch (NoSuchMessageException nsme) {
            System.err.println("No such message in the .properties file");
            nsme.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error while trying to send email");
            e.printStackTrace();
        }
    }
}
