package com.kgc.springboot_1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class UserController {

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/loginController")
    public String doLogin(String username, String pwd, Model model){
        System.out.println("姓名:"+username);
        model.addAttribute(username);
        return "show";
    }
}
