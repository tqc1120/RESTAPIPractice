package com.example.demo.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Test {
    @RequestMapping("hello")
    public String hello(){
        return "hello";
    }
}