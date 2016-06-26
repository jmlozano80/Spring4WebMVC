package com.lozano.service.event;

import com.lozano.model.Account;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

/**
 * Created by jose on 14/05/16.
 */
public class OnResetPasswordEvent extends ApplicationEvent {
    private final String appUrl;
    private final Locale locale;
    private final Account account;

    public OnResetPasswordEvent(Account account, Locale locale, String appUrl) {
        super(account);
        this.account = account;
        this.locale = locale;
        this.appUrl = appUrl;
    }



    public Account getAccount() {
        return account;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getAppUrl() {
        return appUrl;
    }
}