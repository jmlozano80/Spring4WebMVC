package com.lozano.controller;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by jose on 01/07/16.
 */
@RestController
@RequestMapping("admin")
public class AdminController {

    @RequestMapping(value = "/admin1", method = RequestMethod.GET, produces = "application/json")
    public String admin1() {
        System.out.println("Inside admin/admin1 ");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("admin1","admin1 Response" );

        return jsonObject.toString();
    }

    @RequestMapping(value = "/admin2", method = RequestMethod.GET, produces = "application/json")
    public String admin2() {
        System.out.println("Inside admin/admin2 ");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("admin2","admin2 Response" );

        return jsonObject.toString();
    }

    @RequestMapping(value = "/admin3", method = RequestMethod.GET, produces = "application/json")
    public String admin3() {
        System.out.println("Inside admin/admin3 ");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("admin3","admin3 Response" );

        return jsonObject.toString();
    }
}
