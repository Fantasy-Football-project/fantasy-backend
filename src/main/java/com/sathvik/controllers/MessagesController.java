package com.sathvik.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping
public class MessagesController {
    /*
    This method is a get method. At the moment, when a request is made to this url, the
    strings first and second will return. Response Entity returns the status of the request.
    If it is 200, it means it was successful, and unsuccessful otherwise.
     */
    @GetMapping("/messages")
    public ResponseEntity<List<String>> getMessages() {
        return ResponseEntity.ok(Arrays.asList("first", "second"));
    }
}
