package com.sathvik.controllers;

import com.sathvik.entities.League;
import com.sathvik.services.LeagueSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LeagueSettingController {
    private final LeagueSettingService leagueSettingService;
}
