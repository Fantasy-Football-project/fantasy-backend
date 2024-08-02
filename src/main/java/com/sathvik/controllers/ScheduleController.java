package com.sathvik.controllers;

import com.sathvik.entities.WeekMatchups;
import com.sathvik.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/get-all-leagues-matchups")
    public ResponseEntity<Map<Integer, WeekMatchups>> getAllMatchups(@RequestParam String leagueName) {
        return ResponseEntity.ok(scheduleService.getAllMatchups(leagueName));
    }

    @PutMapping("/randomize-league-matchups")
    public void randomizeMatchups(@RequestParam String leagueName) {
        scheduleService.randomizeSchedule(leagueName);
    }
}
