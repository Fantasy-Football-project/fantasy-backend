package com.sathvik.controllers;

import com.sathvik.entities.Player;
import com.sathvik.services.AddPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AddPlayerController {
    private final AddPlayerService addPlayerService;

    @PutMapping("/add-player")
    public ResponseEntity<Player> addPlayer(@RequestParam String leagueName,
                @RequestParam String username, @RequestParam Long playerIdAdd) {
        return ResponseEntity.ok(addPlayerService.addPlayer(leagueName, username, playerIdAdd));
    }

    @PutMapping("/drop-player")
    public ResponseEntity<Player> dropPlayer(@RequestParam String leagueName,
                                            @RequestParam String username, @RequestParam Long playerIdDrop) {
        return ResponseEntity.ok(addPlayerService.dropPlayer(leagueName, username, playerIdDrop));
    }

    @PutMapping("/add-and-drop-players")
    public ResponseEntity<Player> addAndDropPlayer(@RequestParam String leagueName,
               @RequestParam String username, @RequestParam Long playerIdAdd,
                                                   @RequestParam Long playerIdDrop) {
        return ResponseEntity.ok(addPlayerService.addAndDropPlayer(leagueName, username, playerIdAdd, playerIdDrop));
    }
}
