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
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class PlayerController {
    private final PlayerService playerService;
    private final PastPlayerDataService pastPlayerDataService;

    @GetMapping("/get-all-players")
    public ResponseEntity<Page<Player>> getAllPlayers(@RequestParam int pageNumber,
                                                      @RequestParam int pageSize) {
        return ResponseEntity.ok().body(playerService.getAllPlayers(pageNumber, pageSize));
    }

    @GetMapping("/get-all-available-players")
    public ResponseEntity<Page<Player>> getAllAvailablePlayers(@RequestParam String leagueName,
           @RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok().body(playerService.getAllAvailablePlayers(leagueName, pageNumber, pageSize));
    }

    @GetMapping("/past-data")
    public ResponseEntity<List<PastPlayerData>> getPastData(@RequestParam String playerName) {
        return ResponseEntity.ok().body(pastPlayerDataService.getPlayerData(playerName));
    }

    @GetMapping("/past-data-team")
    public ResponseEntity<List<PastPlayerData>> getDSTPastData(@RequestParam String teamName) {
        return ResponseEntity.ok().body(pastPlayerDataService.getDSTData(teamName));
    }

    @GetMapping("/get-player-by-id")
    public ResponseEntity<Optional<Player>> getPlayerById(@RequestParam Long id) {
        return ResponseEntity.ok().body(playerService.getPlayerById(id));
    }
}
