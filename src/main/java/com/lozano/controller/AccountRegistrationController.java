package com.lozano.controller;

import com.lozano.model.Account;
import com.lozano.model.PasswordResetToken;
import com.lozano.model.VerificationToken;
import com.lozano.model.form.RegistrationForm;
import com.lozano.model.form.RequestResetPasswordForm;
import com.lozano.model.form.ResendEmailForm;
import com.lozano.model.form.ResetPasswordForm;
import com.lozano.service.AccountService;
import com.lozano.service.EmailService;
import com.lozano.service.event.OnRegistrationCompleteEvent;
import com.lozano.service.event.OnResendEmailEvent;
import com.lozano.service.event.OnResetPasswordEvent;
import com.lozano.service.exception.EmailExistsException;
import com.lozano.service.exception.EmailNotSentException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;

/**
 * Created by jose on 15/05/16.
 */
@RestController
public class AccountRegistrationController {

    @Autowired
    MessageSource messageSource;

    @Autowired
    EmailService emailService;

    @Autowired
    AccountService accountService;

    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "/requestResetPassword", method = RequestMethod.POST)
    public ResponseEntity<String> requestResetPassword(@Valid @RequestBody RequestResetPasswordForm requestResetPasswordForm,
                                                       BindingResult bResult, HttpServletRequest request) {

        System.out.println("Inside requestResetPassword");
        JSONObject responseJson = new JSONObject();

        if (bResult.getFieldError("email") != null) {
            if (bResult.getFieldError("email").getCode().equals("Pattern"))
                responseJson.put("error", messageSource.getMessage("Pattern.email", null, request.getLocale()));
            else
                responseJson.put("error", bResult.getFieldError("email").getCode());
            return ResponseEntity.status(406).body(responseJson.toJSONString());
        }

        // Check if the account exists
        Account acc = accountService.findAccount(requestResetPasswordForm.getEmail());
        if (acc == null) {
            responseJson.put("error", messageSource.getMessage("request.reset.password.doesNotExist", null, request.getLocale()));
            return ResponseEntity.status(406).body(responseJson.toJSONString());
        }

        String appUrl = request.getRequestURL().toString().split("/requestResetPassword")[0];

        emailService.sendResetPasswordEmail(new OnResetPasswordEvent(acc, request.getLocale(), appUrl));

        // Send the reset password email
        responseJson.put("message", messageSource.getMessage("request.reset.password.success", null, request.getLocale()));
        responseJson.put("success",true);
        return ResponseEntity.status(200).body(responseJson.toJSONString());

    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public ResponseEntity<String> getResetPassword(@RequestParam("token") String token, @RequestParam String email, HttpServletRequest request) {

        System.out.println("Inside GET resetPassword token = "+token);
        JSONObject jsonResponse = new JSONObject();
        PasswordResetToken passwordResetToken = accountService.findPasswordResetToken(token);
        if (passwordResetToken == null) {
            jsonResponse.put("error", messageSource.getMessage("reset.password.token.invalid", null, request.getLocale()));
            return ResponseEntity.status(406).body(jsonResponse.toJSONString());
        }

        Account acc = passwordResetToken.getAcc();
        // Check if expired
        Calendar cal = Calendar.getInstance();
        Long timeDiff = passwordResetToken.getExpiryDate().getTime() - cal.getTime().getTime();
        if (timeDiff <= 0) {
            jsonResponse.put("error", messageSource.getMessage("reset.password.token.expired", null, request.getLocale()));
            return ResponseEntity.status(406).body(jsonResponse.toJSONString());
        }

        // Proceed to authentication
        Authentication auth = new UsernamePasswordAuthenticationToken(acc, null, userDetailsService.loadUserByUsername(acc.getEmail()).getAuthorities());
        System.out.println("Authorities of " + acc.getEmail() + " are : " + auth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        jsonResponse.put("message", messageSource.getMessage("reset.password.success", null, request.getLocale()));
        return ResponseEntity.status(200).body(jsonResponse.toJSONString());
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordForm resetPasswordForm,
                                                BindingResult bResult, HttpServletRequest request, WebRequest webRequest) {

        System.out.println(resetPasswordForm);
        JSONObject responseJson = new JSONObject();

        if (resetPasswordForm.getPassword() == null) {
            bResult.rejectValue("password", messageSource.getMessage("reset.password.password.null", null, request.getLocale()));
        } else if (resetPasswordForm.getConfirmPassword() == null) {
            bResult.rejectValue("confirmPassword", messageSource.getMessage("reset.password.password.null", null, request.getLocale()));
        } else if (!resetPasswordForm.getPassword().equals(resetPasswordForm.getConfirmPassword())) {
            bResult.rejectValue("password", messageSource.getMessage("registration.passwordMismatch", null, webRequest.getLocale()));
            bResult.rejectValue("confirmPassword", messageSource.getMessage("registration.passwordMismatch", null, webRequest.getLocale()));
            System.out.println("Password match validation - failed.");
        }

        if (bResult.hasErrors())
            return processRegistrationBindingResultErrors(bResult, webRequest);

        // Find the token from resetPasswordForm
        PasswordResetToken passwordResetToken = accountService.findPasswordResetToken(resetPasswordForm.getToken());
        if (passwordResetToken == null || passwordResetToken.getAcc() == null) {
            responseJson.put("error", messageSource.getMessage("reset.password.token.invalid", null, request.getLocale()));
            return ResponseEntity.status(406).body(responseJson.toJSONString());
        }

        // Check if the provided email corresponds to the one associated with passwordResetToken
        // nevermind that check, lets allow users to change password just through having a valid token
        /*if (resetPasswordForm.getEmail() == null || !resetPasswordForm.getEmail().equals(passwordResetToken.getAcc().getEmail())) {
            responseJson.put("error", messageSource.getMessage("reset.password.error.email", null, request.getLocale()));
            return ResponseEntity.status(406).body(responseJson.toJSONString());
        }*/

        // Check if expired
        Calendar cal = Calendar.getInstance();
        Long timeDiff = passwordResetToken.getExpiryDate().getTime() - cal.getTime().getTime();
        if (timeDiff <= 0) {
            responseJson.put("error", messageSource.getMessage("reset.password.token.expired", null, request.getLocale()));
            return ResponseEntity.status(406).body(responseJson.toJSONString());
        }

        Account acc = passwordResetToken.getAcc();
        /* Attempt to reset the password */
        acc.setPassword(accountService.encodePassword(resetPasswordForm.getPassword()));
        if (accountService.updateAccount(acc) != null) {
            responseJson.put("message", messageSource.getMessage("reset.password.success", null, request.getLocale()));
            return ResponseEntity.status(200).body(responseJson.toJSONString());
        } else {
            responseJson.put("error", messageSource.getMessage("reset.password.error", null, request.getLocale()));
            return ResponseEntity.status(500).body(responseJson.toJSONString());
        }
    }


    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public ResponseEntity<String> registrationConfirm(@RequestParam("token") String token, HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();

        int noSuchToken = 0;
        int accountIsEnabled = 1;
        int tokenExpired = 2;
        int accountCouldNotBeUpdated = 3;
        int accountUpdated = 4;


        System.out.println("Inside registration confirm");

        // Obtain verification token
        VerificationToken verificationToken = accountService.findVerificationToken(token);
        if (verificationToken == null) {
            // No such token
            String tokenErrorMessage = messageSource.getMessage("registration.invalidToken", null, request.getLocale());
            responseJson.put("tokenError", tokenErrorMessage);
            responseJson.put("tokenMessage", tokenExpired);
            return ResponseEntity.status(406).body(responseJson.toJSONString());
        }

        // Obtain the account associated to the verification token
        Account acc = verificationToken.getAcc();

        if (acc.isEnabled()) {
            responseJson.put("message", messageSource.getMessage("resend.email.alreadyActivated", null, request.getLocale()));
            responseJson.put("token", verificationToken.getToken());
            responseJson.put("tokenMessage", accountIsEnabled);

            return ResponseEntity.status(200).body(responseJson.toJSONString());
        }

        // Ensure token is not expired
        Calendar cal = Calendar.getInstance();
        Long timeDiff = verificationToken.getExpiryDate().getTime() - cal.getTime().getTime();
        if (timeDiff <= 0) {
            responseJson.put("tokenError", messageSource.getMessage("registration.tokenExpired", null, request.getLocale()));
            responseJson.put("tokenMessage", noSuchToken);
            String appUrl = request.getRequestURL().toString().split("/registrationConfirm")[0];
            // Send email
            try {
                emailService.sendConfirmationEmail(new OnRegistrationCompleteEvent(verificationToken.getAcc(), request.getLocale(), appUrl));
            } catch (EmailNotSentException e) {
                e.printStackTrace();
            }
            return ResponseEntity.status(406).body(responseJson.toJSONString());
        }

        // Activate account
        acc.setEnabled(true);
        Account updatedAccount = accountService.updateAccount(acc);
        if (updatedAccount == null) {
            responseJson.put("error", messageSource.getMessage("registration.couldNotBeActivated", null, request.getLocale()));
            responseJson.put("tokenMessage", accountCouldNotBeUpdated);
            return ResponseEntity.status(406).body(responseJson.toJSONString());
        }

        responseJson.put("message", messageSource.getMessage("registration.activated", null, request.getLocale()));
        responseJson.put("token", verificationToken.getToken());
        responseJson.put("success", true);
        responseJson.put("tokenMessage", accountUpdated);
        System.out.println("account is enabled");

        return ResponseEntity.status(200).body(responseJson.toJSONString());
    }

    @RequestMapping(value = "/resendConfirmationEmail", method = RequestMethod.POST)
    public ResponseEntity<String> resendConfirmationEmail(@Valid @RequestBody ResendEmailForm resendEmailForm, BindingResult bResult, HttpServletRequest request) {
        JSONObject responseJson = new JSONObject();
        if (bResult.getFieldError("email") != null) {
            if (bResult.getFieldError("email").getCode().equals("Pattern"))
                responseJson.put("emailError", messageSource.getMessage("Pattern.email", null, request.getLocale()));
            else
                responseJson.put("emailError", bResult.getFieldError("email").getCode());
            return ResponseEntity.status(406).body(responseJson.toJSONString());
        }
        Account acc = accountService.findAccount(resendEmailForm.getEmail());
        if (acc == null) {
            responseJson.put("emailError", messageSource.getMessage("resend.email.doesNotExist", null, request.getLocale()));
            return ResponseEntity.status(406).body(responseJson.toJSONString());
        } else if (acc.isEnabled()) {
            responseJson.put("message", messageSource.getMessage("resend.email.alreadyActivated", null, request.getLocale()));
            return ResponseEntity.status(200).body(responseJson.toJSONString());
        }

        // No error & account is disabled
        String appUrl = request.getRequestURL().toString().split("/resendConfirmationEmail")[0];
        // Attempt to resend the activation email
        emailService.resendConfirmationEmail(new OnResendEmailEvent(acc, request.getLocale(), appUrl));

        responseJson.put("message", messageSource.getMessage("resend.email.success", null, request.getLocale()));
        return ResponseEntity.status(200).body(responseJson.toJSONString());
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegistrationForm registrationForm, BindingResult bResult, HttpServletRequest request, WebRequest webRequest) {

        System.out.println("Inside /register");
        System.out.println("registrationForm "+ registrationForm.getEmail());
        JSONObject responseJson = new JSONObject();

        if (!registrationForm.getPassword().equals(registrationForm.getConfirmPassword())) {
            bResult.rejectValue("password", messageSource.getMessage("registration.passwordMismatch", null, webRequest.getLocale()));
            bResult.rejectValue("confirmPassword", messageSource.getMessage("registration.passwordMismatch", null, webRequest.getLocale()));
            System.out.println("Password match validation - failed.");
        }

        if (bResult.hasErrors())
            return processRegistrationBindingResultErrors(bResult, webRequest);

        /* Attempt to create the account */
        Account newAcc = new Account();
        newAcc.setEmail(registrationForm.getEmail());
        newAcc.setPassword(registrationForm.getPassword());
        newAcc.setUserName(registrationForm.getUserName());

        try {// Attempt acc creation
            Account acc = accountService.createAccount(newAcc);
            if (acc != null) {
                // Acc created
                String appUrl = request.getRequestURL().toString().split("/register")[0];
                System.out.println("APP URL = " + appUrl);

                /*ProviderSignInUtils.handlePostSignUp(acc.getEmail(), request);*/
                emailService.sendConfirmationEmail(new OnRegistrationCompleteEvent(newAcc, request.getLocale(), appUrl));
                // Forward to the login, acknowleding that account has been created
                responseJson.put("message", messageSource.getMessage("registration.created", null, request.getLocale()));
            } else { // Acc not created
                bResult.rejectValue("email", "registration.accCreationError");
            }
        }  catch(EmailExistsException eee) {
            System.out.println("Catched EmailexistsException, attaching the rejected value to BindingREsult");
            bResult.rejectValue("email", messageSource.getMessage("registration.emailExists", null,"placeDetails", request.getLocale()));
        } catch (EmailNotSentException uee) {
            System.out.println("Catched EmailNotSentException, attaching the rejected value to BindingResult");
            bResult.rejectValue("email", messageSource.getMessage("registration.emailCouldNotBeSent", null, request.getLocale()));
        }

        if (bResult.hasErrors())
            return processRegistrationBindingResultErrors(bResult, webRequest);


        responseJson.put("email", accountService.findAccount(registrationForm.getEmail()).getEmail());
        responseJson.put("success", true);
        return ResponseEntity.ok(responseJson.toJSONString());
    }

    private ResponseEntity<String> processRegistrationBindingResultErrors(BindingResult bResult, WebRequest webRequest) {
        // Process errors
        System.out.println(bResult.getAllErrors().toString());
        JSONObject json = new JSONObject();

        if (bResult.getFieldError("email") != null) {
            if (bResult.getFieldError("email").getCode().equals("Pattern"))
                json.put("message", messageSource.getMessage("Pattern.email", null, webRequest.getLocale()));
            else
                json.put("message", bResult.getFieldError("email").getCode());
        }
        if (bResult.getFieldError("password") != null) {
            if (bResult.getFieldError("password").getCode().equals("Size"))
                json.put("message", messageSource.getMessage("Size.password", null, webRequest.getLocale()));
            else
                json.put("message", bResult.getFieldError("password").getCode());
        }
        if (bResult.getFieldError("confirmPassword") != null) {
            if (bResult.getFieldError("confirmPassword").getCode().equals("Size"))
                json.put("message", messageSource.getMessage("Size.confirmPassword", null, webRequest.getLocale()));
            else
                json.put("messager", bResult.getFieldError("confirmPassword").getCode());
        }
        return ResponseEntity.status(406).body(json.toJSONString());
    }

}