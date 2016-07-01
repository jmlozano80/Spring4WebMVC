package com.lozano.controller;

import com.lozano.service.AccountService;
import com.lozano.service.EmailService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by jose on 14/05/16.
 */
@RestController
public class AccountController {

    @Autowired
    MessageSource messageSource;

    @Autowired
    EmailService emailService;

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/api/user", method = RequestMethod.GET, produces = "application/json")
    public String user(Principal user) {
        System.out.println("Inside api/user "+user);
        if (user != null)
            System.out.println("User is authenticated : " + user.getName());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", (user != null) ? user.getName() : "");
        jsonObject.put("success", (user != null) ? true : false);
        return jsonObject.toString();
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String deleteAllAccounts() {

        JSONObject jsonObject =  new JSONObject();
        jsonObject.put("test","test");
        return jsonObject.toString();
    }

    @RequestMapping(value = "/api/username", method = RequestMethod.GET, produces = "application/json")
    public String userName(Principal user) {
        System.out.println("Inside api/user");
        if (user != null)
            System.out.println("User is authenticated : " + user.getName());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", (user != null) ? accountService.getUserNameByEmail(user.getName()) : "");
        return jsonObject.toString();
    }
    @RequestMapping(value = "/api/isadmin", method = RequestMethod.GET, produces = "application/json")
    public String isUserAdmin(Principal user) {
        System.out.println("Inside api/isadmin");
        if (user != null)
            System.out.println("User is authenticated : " + user.getName());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isAdmin", (user != null) ? accountService.isUserAdmin(user.getName()) : "");
        return jsonObject.toString();
    }
}
