package com.sathvik.services;

import com.sathvik.entities.League;
import com.sathvik.entities.Player;
import com.sathvik.entities.Team;
import com.sathvik.repositories.LeagueRepository;
import com.sathvik.repositories.PlayerRepository;
import com.sathvik.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class AddPlayerService {
    private final LeagueRepository leagueRepository;
    private final PlayerRepository playerRepository;
    private final TeamService teamService;
    private final TeamRepository teamRepository;

    // The purpose of this method is to add a player to a team. Method should only be called when
    // the team adding the player has at least one extra roster spot available. Therefore, there
    // are no checks for this. A different method will be used to add and drop players if the roster
    // does not have the space to add someone.
    public Player addPlayer(String leagueName, String username, Long playerId, Boolean fromTrade) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new IllegalArgumentException("League not found"));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        Team team = teamService.findTeam(leagueName, username);

        team.getTeamPlayers().add(player);
        team.getBench().add(player);
        league.getAvailablePlayers().remove(player);
        league.getTakenPlayers().add(player);
        player.getTakenLeagues().add(league);
        player.getAvailableLeagues().add(league);

        teamRepository.save(team);
        playerRepository.save(player);

        Date date = new Date();

        if (!fromTrade) {
            league.getRecentActivity().add(date + ": Team: " + team.getTeamName() + " adds Player: "
                    + player.getFullName());
        }
        leagueRepository.save(league);

        return player;
    }

    public Player addAndDropPlayer(String leagueName, String username, Long playerIdAdd,
                                   Long playerIdDrop) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new IllegalArgumentException("League not found"));
        Player playerToAdd = playerRepository.findById(playerIdAdd)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        Player playerToDrop = playerRepository.findById(playerIdDrop)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        Team team = teamService.findTeam(leagueName, username);

        dropPlayer(leagueName, username, playerIdDrop, false);
        team.getTeamPlayers().add(playerToAdd);
        team.getBench().add(playerToAdd);

        league.getAvailablePlayers().remove(playerToAdd);
        league.getTakenPlayers().add(playerToAdd);

        playerToAdd.getTakenLeagues().add(league);
        playerToAdd.getAvailableLeagues().remove(league);
        playerToDrop.getTakenLeagues().remove(league);
        playerToDrop.getAvailableLeagues().add(league);

        playerRepository.save(playerToAdd);
        teamRepository.save(team);

        Date date = new Date();

        league.getRecentActivity().add(date + ": Team: " + team.getTeamName() + " adds Player: " +
                playerToAdd.getFullName());

        leagueRepository.save(league);

        return playerToAdd;
    }

    public Player dropPlayer(String leagueName, String username, Long playerIdDrop, Boolean fromTrade) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new IllegalArgumentException("League not found"));
        Player playerToDrop = playerRepository.findById(playerIdDrop)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        Team team = teamService.findTeam(leagueName, username);

        team.getStartingQB().remove(playerToDrop);
        team.getStartingRB().remove(playerToDrop);
        team.getStartingWR().remove(playerToDrop);
        team.getStartingTE().remove(playerToDrop);
        team.getStartingFLEX().remove(playerToDrop);
        team.getStartingK().remove(playerToDrop);
        team.getStartingDST().remove(playerToDrop);
        team.getBench().remove(playerToDrop);
        team.getTeamPlayers().remove(playerToDrop);

        league.getAvailablePlayers().add(playerToDrop);
        league.getTakenPlayers().remove(playerToDrop);

        playerToDrop.getTakenLeagues().remove(league);
        playerToDrop.getAvailableLeagues().add(league);

        leagueRepository.save(league);
        playerRepository.save(playerToDrop);
        teamRepository.save(team);

        Date date = new Date();

        if (!fromTrade) {
            league.getRecentActivity().add(date + ": Team: " + team.getTeamName() + " drops Player: " +
                    playerToDrop.getFullName());
        }

        return playerToDrop;
    }
}
