package com.sathvik.controllers;

import com.sathvik.dto.CreateLeagueDto;
import com.sathvik.dto.UserDto;
import com.sathvik.entities.League;
import com.sathvik.services.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class LeagueController {

    private final LeagueService leagueService;

    @GetMapping("/{user_id}")
    public ResponseEntity<List<League>> getLeagues(@PathVariable Long user_id) {
        List<League> leagues = leagueService.getAllLeagues(user_id);

        //Checking to see if the league exists.
        if (leagues != null) {
            return ResponseEntity.ok().body(leagues);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create-league")
    public ResponseEntity<League> createLeague(@RequestBody CreateLeagueDto league) {
        League createdLeague = leagueService.createLeague(league);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdLeague.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdLeague);
    }
}
