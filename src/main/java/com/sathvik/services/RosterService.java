package com.sathvik.services;

import com.sathvik.entities.Player;
import com.sathvik.entities.Team;
import com.sathvik.exceptions.AppException;
import com.sathvik.repositories.PlayerRepository;
import com.sathvik.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RosterService {

    private final TeamRepository teamRepository;
    private final TeamService teamService;
    private final PlayerRepository playerRepository;

    // The purpose of this method is to adjust the starting lineup of a fantasy roster. This
    // method specifically returns the list of players that the selected player can be swapped with.
    public List<Player> editLineupAllowed (String leagueName, String username, Long playerId) {
        Team team = teamService.findTeam(leagueName, username);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new AppException("Unknown Player", HttpStatus.NOT_FOUND));

        List<Player> result = new ArrayList<>();

        if (player.getPosition().equals("QB")) {
            if (!team.getBench().contains(player)) {
                for (Player p : team.getBench()) {
                    if (player.getPosition().equals(p.getPosition())) {
                        result.add(p);
                    }
                }
            }
            if (!team.getStartingQB().contains(player)) {
                result.addAll(team.getStartingQB());
            }
        }
        else if (player.getPosition().equals("RB")) {
            editLineupAllowedHelper(team, player, result);
            if (!team.getStartingRB().contains(player)) {
                result.addAll(team.getStartingRB());
            }
        }
        else if (player.getPosition().equals("WR")) {
            editLineupAllowedHelper(team, player, result);
            if (!team.getStartingWR().contains(player)) {
                result.addAll(team.getStartingWR());
            }
        }
        else if (player.getPosition().equals("TE")) {
            editLineupAllowedHelper(team, player, result);
            if (!team.getStartingTE().contains(player)) {
                result.addAll(team.getStartingTE());
            }
        }
        else if (player.getPosition().equals("K")) {
            if (!team.getBench().contains(player)) {
                for (Player p : team.getBench()) {
                    if (player.getPosition().equals(p.getPosition())) {
                        result.add(p);
                    }
                }
            }
            if (!team.getStartingK().contains(player)) {
                result.addAll(team.getStartingK());
            }

        }
        else if (player.getPosition().equals("DST")) {
            if (!team.getBench().contains(player)) {
                for (Player p : team.getBench()) {
                    if (player.getPosition().equals(p.getPosition())) {
                        result.add(p);
                    }
                }
            }
            if (!team.getStartingDST().contains(player)) {
                result.addAll(team.getStartingDST());
            }
        }

        return result;
    }

    private void editLineupAllowedHelper(Team team, Player player, List<Player> result) {
        if (!team.getBench().contains(player)) {
            for (Player p : team.getBench()) {
                if (player.getPosition().equals(p.getPosition())) {
                    result.add(p);
                }
            }
        }
        if (!team.getStartingFLEX().contains(player)) {
            result.addAll(team.getStartingFLEX());
        }
    }

    public void editLineupSwap (String leagueName, String username, Long playerIdOne,
                                  Long playerIdTwo) {
        Team team = teamService.findTeam(leagueName, username);
        Player playerOne = playerRepository.findById(playerIdOne)
                .orElseThrow(() -> new AppException("Unknown Player", HttpStatus.NOT_FOUND));
        Player playerTwo = playerRepository.findById(playerIdTwo)
                .orElseThrow(() -> new AppException("Unknown Player", HttpStatus.NOT_FOUND));

        if (playerOne.getPosition().equals(playerTwo.getPosition())) {
            // Checking QB swap.
            if (playerOne.getPosition().equals("QB")) {
                swapQB(team, playerOne, playerTwo);
            }
            // Checking all possible RB swaps.
            else if (playerOne.getPosition().equals("RB")) {
                swapRB(team, playerOne, playerTwo);
            }
            // Checking all possible WR swaps.
            else if (playerOne.getPosition().equals("WR")) {
                swapWR(team, playerOne, playerTwo);
            }
            // Checking all possible TE swaps.
            else if (playerOne.getPosition().equals("TE")) {
                swapTE(team, playerOne, playerTwo);
            }
            else if (playerOne.getPosition().equals("K")) {
                swapK(team, playerOne, playerTwo);
            }
            else if (playerOne.getPosition().equals("DST")) {
                swapDST(team, playerOne, playerTwo);
            }
        }
        else {
            if (team.getStartingFLEX().contains(playerOne)) {
                flexHelperTwo(team, playerTwo, playerOne);
            }
            else {
                flexHelperTwo(team, playerOne, playerTwo);
            }
        }

        teamRepository.save(team);
    }

    private void flexHelperTwo(Team team, Player playerOne, Player playerTwo) {
        team.getStartingFLEX().remove(playerTwo);
        team.getBench().add(playerTwo);
        if (playerOne.getPosition().equals("RB")) {
            team.getStartingRB().remove(playerOne);
            team.getStartingFLEX().add(playerOne);
        }
        else if (playerOne.getPosition().equals("WR")) {
            team.getStartingWR().remove(playerOne);
            team.getStartingFLEX().add(playerOne);
        }
        else if (playerOne.getPosition().equals("TE")) {
            team.getStartingTE().remove(playerOne);
            team.getStartingFLEX().add(playerOne);
        }
    }

    private void swapQB (Team team,Player playerOne,
                        Player playerTwo) {
        if (team.getStartingQB().contains(playerOne) && team.getBench().contains(playerTwo)) {
            team.getStartingQB().remove(playerOne);
            team.getStartingQB().add(playerTwo);
            team.getBench().add(playerOne);
            team.getBench().remove(playerTwo);
        }
        else if (team.getStartingQB().contains(playerTwo) && team.getBench().contains(playerOne)) {
            team.getStartingQB().remove(playerTwo);
            team.getStartingQB().add(playerOne);
            team.getBench().add(playerTwo);
            team.getBench().remove(playerOne);
        }
    }

    private void swapRB (Team team,Player playerOne,
                        Player playerTwo) {
        if (team.getStartingRB().contains(playerOne) && team.getBench().contains(playerTwo)) {
            team.getStartingRB().remove(playerOne);
            team.getStartingRB().add(playerTwo);
            team.getBench().add(playerOne);
            team.getBench().remove(playerTwo);
        }
        else if (team.getStartingRB().contains(playerTwo) && team.getBench().contains(playerOne)) {
            team.getStartingRB().remove(playerTwo);
            team.getStartingRB().add(playerOne);
            team.getBench().add(playerTwo);
            team.getBench().remove(playerOne);
        }
        else if (team.getStartingRB().contains(playerOne) && team.getStartingFLEX().contains(playerTwo)) {
            team.getStartingRB().remove(playerOne);
            team.getStartingRB().add(playerTwo);
            team.getStartingFLEX().add(playerOne);
            team.getStartingFLEX().remove(playerTwo);
        }
        else if (team.getStartingFLEX().contains(playerTwo) && team.getStartingRB().contains(playerOne)) {
            team.getStartingFLEX().remove(playerTwo);
            team.getStartingFLEX().add(playerOne);
            team.getStartingRB().add(playerTwo);
            team.getStartingRB().remove(playerOne);
        }
        swapFlexBenchHelper(team, playerOne, playerTwo);
    }

    private void swapWR (Team team,Player playerOne,
                        Player playerTwo) {
        if (team.getStartingWR().contains(playerOne) && team.getBench().contains(playerTwo)) {
            team.getStartingWR().remove(playerOne);
            team.getStartingWR().add(playerTwo);
            team.getBench().add(playerOne);
            team.getBench().remove(playerTwo);
        }
        else if (team.getStartingWR().contains(playerTwo) && team.getBench().contains(playerOne)) {
            team.getStartingWR().remove(playerTwo);
            team.getStartingWR().add(playerOne);
            team.getBench().add(playerTwo);
            team.getBench().remove(playerOne);
        }
        else if (team.getStartingWR().contains(playerOne) && team.getStartingFLEX().contains(playerTwo)) {
            team.getStartingWR().remove(playerOne);
            team.getStartingWR().add(playerTwo);
            team.getStartingFLEX().add(playerOne);
            team.getStartingFLEX().remove(playerTwo);
        }
        else if (team.getStartingFLEX().contains(playerTwo) && team.getStartingWR().contains(playerOne)) {
            team.getStartingFLEX().remove(playerTwo);
            team.getStartingFLEX().add(playerOne);
            team.getStartingWR().add(playerTwo);
            team.getStartingWR().remove(playerOne);
        }
        swapFlexBenchHelper(team, playerOne, playerTwo);
    }

    private void swapTE (Team team,Player playerOne,
                        Player playerTwo) {
        if (team.getStartingTE().contains(playerOne) && team.getBench().contains(playerTwo)) {
            team.getStartingTE().remove(playerOne);
            team.getStartingTE().add(playerTwo);
            team.getBench().add(playerOne);
            team.getBench().remove(playerTwo);
        }
        else if (team.getStartingTE().contains(playerTwo) && team.getBench().contains(playerOne)) {
            team.getStartingTE().remove(playerTwo);
            team.getStartingTE().add(playerOne);
            team.getBench().add(playerTwo);
            team.getBench().remove(playerOne);
        }
        else if (team.getStartingTE().contains(playerOne) && team.getStartingFLEX().contains(playerTwo)) {
            team.getStartingTE().remove(playerOne);
            team.getStartingTE().add(playerTwo);
            team.getStartingFLEX().add(playerOne);
            team.getStartingFLEX().remove(playerTwo);
        }
        else if (team.getStartingFLEX().contains(playerTwo) && team.getStartingTE().contains(playerOne)) {
            team.getStartingFLEX().remove(playerTwo);
            team.getStartingFLEX().add(playerOne);
            team.getStartingTE().add(playerTwo);
            team.getStartingTE().remove(playerOne);
        }
        swapFlexBenchHelper(team, playerOne, playerTwo);
    }

    private void swapFlexBenchHelper (Team team,Player playerOne,
                        Player playerTwo) {
        if (team.getStartingFLEX().contains(playerOne) && team.getBench().contains(playerTwo)) {
            team.getStartingFLEX().remove(playerOne);
            team.getStartingFLEX().add(playerTwo);
            team.getBench().add(playerOne);
            team.getBench().remove(playerTwo);
        }
        else if (team.getStartingFLEX().contains(playerTwo) && team.getBench().contains(playerOne)) {
            team.getStartingFLEX().remove(playerTwo);
            team.getStartingFLEX().add(playerOne);
            team.getBench().add(playerTwo);
            team.getBench().remove(playerOne);
        }
    }

    private void swapK (Team team,Player playerOne,
                        Player playerTwo) {
        if (team.getStartingK().contains(playerOne) && team.getBench().contains(playerTwo)) {
            team.getStartingK().remove(playerOne);
            team.getStartingK().add(playerTwo);
            team.getBench().add(playerOne);
            team.getBench().remove(playerTwo);
        }
        else if (team.getStartingK().contains(playerTwo) && team.getBench().contains(playerOne)) {
            team.getStartingK().remove(playerTwo);
            team.getStartingK().add(playerOne);
            team.getBench().add(playerTwo);
            team.getBench().remove(playerOne);
        }
    }

    private void swapDST (Team team,Player playerOne,
                        Player playerTwo) {
        if (team.getStartingDST().contains(playerOne) && team.getBench().contains(playerTwo)) {
            team.getStartingDST().remove(playerOne);
            team.getStartingDST().add(playerTwo);
            team.getBench().add(playerOne);
            team.getBench().remove(playerTwo);
        }
        else if (team.getStartingDST().contains(playerTwo) && team.getBench().contains(playerOne)) {
            team.getStartingDST().remove(playerTwo);
            team.getStartingDST().add(playerOne);
            team.getBench().add(playerTwo);
            team.getBench().remove(playerOne);
        }
    }
}
