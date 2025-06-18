package com.sathvik.controllers;

import com.sathvik.services.UpdateNFLPlayersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/players")
public class UpdateNFLPlayersController {

    private final UpdateNFLPlayersService playerService;

    public UpdateNFLPlayersController(UpdateNFLPlayersService playerService) {
        this.playerService = playerService;
    }

    // POST endpoint to upload CSV file and import players
    @PostMapping("/import")
    public ResponseEntity<String> importPlayers(@RequestParam("file") MultipartFile file) {
        try {
            playerService.importPlayersFromCsv(file.getInputStream());
            return ResponseEntity.ok("Players imported successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to import players: " + e.getMessage());
        }
    }
}
