package com.sathvik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//just must do
@SpringBootApplication // combines @configuration, @EnableAutoConfig, @ComponentScan
@RestController //anything with get mapping, post mapping, will be exposed as rest endpoints that clients can call
//this means that all methods in class return a json response
@RequestMapping()
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
