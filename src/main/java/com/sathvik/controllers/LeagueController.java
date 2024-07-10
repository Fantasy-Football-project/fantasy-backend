package com.sathvik.controllers;

import com.sathvik.dto.CreateLeagueDto;
import com.sathvik.dto.CreateTeamDto;
import com.sathvik.dto.UserDto;
import com.sathvik.entities.League;
import com.sathvik.entities.Player;
import com.sathvik.services.LeagueService;
import com.sathvik.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

//Change routing to overall leagues and then add/get. Can keep consistent with future
//controllers.
@RequiredArgsConstructor
@RestController
public class LeagueController {

    private final LeagueService leagueService;
    private final UserService userService;

    /*
    This method is a get method to display the leagues that a user is in. It takes
    in a String parameter, the username of the user, and using a UserService object, it
    finds the id of the user. Through that, we can identify which leagues should be displayed.
     */
    @GetMapping("/leagues")
    public ResponseEntity<List<League>> getLeagues(@RequestParam String username) {
        Long userId = userService.findByLogin(username).getId();
        List<League> leagues = leagueService.getLeagues(userId);

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

    @GetMapping("/get-league")
    public ResponseEntity<League> getLeague(@RequestParam String leagueName) {
        return ResponseEntity.ok().body(leagueService.getLeague(leagueName));
    }

    @PutMapping("/join-league")
    public ResponseEntity<League> joinLeague(@RequestBody CreateTeamDto team) {
        leagueService.joinLeague(team);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-all-players")
    public ResponseEntity<List<Player>> getAllPlayers(@RequestParam String leagueName) {
       return ResponseEntity.ok().body(leagueService.getAllPlayers(leagueName));
    }


    @GetMapping("/allLeagues")
    public ResponseEntity<List<League>> getAllLeagues() {
        List<League> leagues = leagueService.getAllLeagues();

        //Checking to see if the league exists.
        if (leagues != null) {
            return ResponseEntity.ok().body(leagues);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
