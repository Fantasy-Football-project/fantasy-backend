package com.sathvik.controllers;

import com.sathvik.entities.Player;
import com.sathvik.services.RosterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RosterController {
    private final RosterService rosterService;

    @GetMapping("/find-allowed-swaps")
    public ResponseEntity<List<Player>> findAllowedSwaps(String leagueName, String username, Long playerId) {
        return ResponseEntity.ok(rosterService.editLineupAllowed(leagueName, username, playerId));
    }

    @PutMapping("/swap-lineup")
    public ResponseEntity<Player> swapLineup(String leagueName, String username,
                                             Long playerIdOne, Long playerIdTwo) {
        rosterService.editLineupSwap(leagueName, username, playerIdOne, playerIdTwo);
        return ResponseEntity.ok().build();
    }
}
