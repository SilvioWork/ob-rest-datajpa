package com.example.obrestdatajpa.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${app.nombrevariable}")
    private String globalVar;

    @GetMapping("/hello")
    public String helloWorld(){
        System.out.println(globalVar);
        return "Hello World desde Spring Boot";
    }
}
