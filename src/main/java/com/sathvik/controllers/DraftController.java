package com.sathvik.controllers;

import com.sathvik.entities.League;
import com.sathvik.entities.Team;
import com.sathvik.services.DraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class DraftController {
    private final DraftService draftService;

    @PutMapping("/randomize-draft-order")
    public ResponseEntity<Map<Integer, Team>> randomizeDraftOrder(@RequestParam String leagueName) {
        Map<Integer, Team> map = draftService.randomize(leagueName);
        return ResponseEntity.ok(map);
    }
}
