package com.lozano.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jose on 14/05/16.
 */
public class RestAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Autowired
    MessageSource messageSource;

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        if (req instanceof HttpServletRequest && httpRequest.getHeader("Origin") != null)  {
            response.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));
        } else {
            response.setHeader("Access-Control-Allow-Origin", "*");
        }
        System.out.println("2INSIDE RESTAUTHENTRYPOINT");
        /*response.addHeader("Access-Control-Allow-Origin", "*");*/
        response.addHeader("WWW-Authenticate", "RestBasic realm=\"" + getRealmName() + "\"");
        if (authException instanceof DisabledException) {
                response.setHeader("error", messageSource.getMessage("login.disabled", null, httpRequest.getLocale()));
            System.out.println("ACOOUNT DISABLED");
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentLength(0);
    }
}
