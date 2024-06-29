package com.sathvik.controllers;

import com.sathvik.dto.UserDto;
import com.sathvik.entities.League;
import com.sathvik.services.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class LeagueController {

    private final LeagueService leagueService;

    @GetMapping("/{league_id}")
    public ResponseEntity<League> getLeagueById(@PathVariable Long league_id) {
        League league = leagueService.getLeagueById(league_id);

        //Checking to see if the league exists.
        if (league != null) {
            return ResponseEntity.ok().body(league);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create-league")
    public ResponseEntity<League> createLeague(@RequestBody League league) {
        /*UserDto user = userService.register(userDto);

        //Once registered, a fresh JWT is made.
        user.setToken(userAuthProvider.createToken(user.getLogin()));

        //When creating an entity (in this case a user), it is best practice to return
        //a 201 HTTP code, with the URL where we can find the new entity.
        return ResponseEntity.created(URI.create("/users/" + user.getId()))
                .body(user); */

        League createdLeague = leagueService.createLeague(league);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdLeague.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdLeague);
    }
}
