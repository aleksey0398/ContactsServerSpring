package com.samsung.contacts_server_spring;

import Authorization.AuthorizationAnswer;
import Authorization.AuthorizationSQLHelper;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class AuthorizationController {

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseBody
    String registration(@RequestParam("login") String login, @RequestParam("password") String passwordMD5) {
        AuthorizationAnswer answer = new AuthorizationAnswer();
        try {
            answer = new AuthorizationSQLHelper().registerNewUser(login, passwordMD5);
            answer.setResult(true);
            answer.setCause("");
        } catch (SQLException e) {
            answer.setResult(false);
            answer.setCause(e.getMessage());
            e.printStackTrace();
        }
        return new Gson().toJson(answer);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    String login(@RequestParam("login") String login, @RequestParam("password") String passwordMD5) {
        AuthorizationAnswer answer = new AuthorizationAnswer();

        try {
            answer = new AuthorizationSQLHelper().login(login, passwordMD5);
        } catch (SQLException e) {
            answer.setResult(false);
            answer.setCause(e.getMessage());
            e.printStackTrace();
        }
        return new Gson().toJson(answer);
    }

    @RequestMapping(value = "/check_login", method = RequestMethod.POST)
    @ResponseBody
    AuthorizationAnswer checkLoginEmployment(@RequestParam("login") String login) {
        AuthorizationAnswer answer = new AuthorizationAnswer();

        try {
            answer = new AuthorizationSQLHelper().checkLoginEmployment(login);
        } catch (SQLException e) {
            e.printStackTrace();
            answer.setResult(false).setCause(e.getMessage());
        }

        return answer;
    }

    @RequestMapping(value = "/check_session", method = RequestMethod.POST)
    @ResponseBody
    AuthorizationAnswer checkSession(@RequestParam("session")String session,@RequestParam("login")String login){
        AuthorizationAnswer answer = new AuthorizationAnswer();
        try {
            answer = new AuthorizationSQLHelper().checkSession(session,login);
        } catch (SQLException e) {
            e.printStackTrace();
            answer.setResult(false).setCause(e.getMessage());
        }
        return answer;
    }

}
