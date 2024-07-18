package com.sathvik.controllers;

import com.sathvik.entities.League;
import com.sathvik.entities.Team;
import com.sathvik.services.DraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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

    @PutMapping("/set-draft-date")
    public ResponseEntity<Date> setDraftDate(@RequestParam String leagueName,
             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date draftDate) {
        draftService.setDraftDate(leagueName, draftDate);
        return ResponseEntity.ok(draftDate);
    }
}
