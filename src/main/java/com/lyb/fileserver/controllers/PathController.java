package com.lyb.fileserver.controllers;

import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.HashMap;

/**
 * Created by  on 11:16 2019/6/19.
 */
@RequestMapping("page")
@Controller
public class PathController {

    @RequestMapping("/")
    public String index() {
        return "/index";
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @RequestMapping("loginValiData")
    public String loginValiData(@RequestParam("pass") String pass, HttpServletRequest request){
        request.getSession().setAttribute("adminpass",pass);
        return "login";
    }

    @RequestMapping("{page}")
    public String info(@PathVariable("page") String page){
        return page;
    }
}
