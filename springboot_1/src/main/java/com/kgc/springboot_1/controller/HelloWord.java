package com.kgc.springboot_1.controller;

import com.kgc.springboot_1.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Controller+@ResponsBody
 */
@RestController
public class HelloWord {

    @RequestMapping("/a")
    public String hello(){
        return "Hello SpringBoot!";
    }


    @RequestMapping("/list")
    public List<User> getList(){
        List<User> list=new ArrayList<>();
        list.add(new User(1,"哈哈"));
        list.add(new User(2,"嘿嘿"));
        list.add(new User(3,"呵呵"));
        list.add(new User(4,"嘻嘻"));
        return list;
    }
}
