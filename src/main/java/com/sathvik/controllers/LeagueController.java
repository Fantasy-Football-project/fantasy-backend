package com.sathvik.controllers;

import com.sathvik.entities.League;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LeagueController {

    @PostMapping("/create-league")
    public String createLeague(@RequestBody League league) {
        return "League goes here";
    }
}
