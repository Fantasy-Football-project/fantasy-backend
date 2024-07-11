package com.sathvik.controllers;

import com.sathvik.entities.PastPlayerData;
import com.sathvik.entities.Player;
import com.sathvik.services.PastPlayerDataService;
import com.sathvik.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PlayerController {
    private final PlayerService playerService;
    private final PastPlayerDataService pastPlayerDataService;

    @GetMapping("/get-all-players")
    public ResponseEntity<Page<Player>> getAllPlayers(@RequestParam(defaultValue = "0") int pageNumber,
                                                      @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok().body(playerService.getAllPlayers(pageNumber, pageSize));
    }

    //TESTING METHOD
    @GetMapping("/past-data")
    public ResponseEntity<List<PastPlayerData>> getPastData(@RequestParam String playerName) {
        return ResponseEntity.ok().body(pastPlayerDataService.getPlayerData(playerName));
    }
}
