package com.example.demo.controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class Test {
    @RequestMapping("hello")
    public String hello(){
        return "hello";
    }
}