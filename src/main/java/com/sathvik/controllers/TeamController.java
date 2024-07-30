package com.sathvik.controllers;

import com.sathvik.entities.Player;
import com.sathvik.entities.Team;
import com.sathvik.services.TeamService;
import com.sathvik.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;

    @GetMapping("/get-roster")
    public ResponseEntity<List<Player>> getPlayers(@RequestParam String leagueName, @RequestParam String username) {
        Team team = teamService.findTeam(leagueName, username);

        List<Player> players = team.getTeamPlayers();

        return ResponseEntity.ok(players);
    }

    @GetMapping("/get-team")
    public ResponseEntity<Team> getTeam(@RequestParam String leagueName, @RequestParam String username) {
        Team team = teamService.findTeam(leagueName, username);

        return ResponseEntity.ok(team);
    }

    @GetMapping("/get-team-by-id")
    public ResponseEntity<Team> getTeam(@RequestParam Long teamId) {
        Team team = teamService.findTeam(teamId);

        return ResponseEntity.ok(team);
    }
}
