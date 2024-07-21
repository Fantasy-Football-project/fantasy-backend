package com.sathvik.services;

import com.sathvik.entities.League;
import com.sathvik.entities.Player;
import com.sathvik.entities.Team;
import com.sathvik.exceptions.AppException;
import com.sathvik.repositories.LeagueRepository;
import com.sathvik.repositories.PlayerRepository;
import com.sathvik.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RequiredArgsConstructor
@Service
public class DraftService {
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    // The purpose of this method is to set the draft date.
    public void setDraftDate(String leagueName,
                             Date draftDate) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new AppException("Unknown league", HttpStatus.NOT_FOUND));

        if (!league.getDraftOrder().isEmpty() && league.getTeams().size() == league.getNumTeams()) {
            league.setDraftDate(draftDate);
            leagueRepository.save(league);
        }
        else {
            if (league.getDraftOrder().isEmpty()) {
                throw new AppException("Draft order not set.", HttpStatus.NOT_FOUND);
            }
            else {
                throw new AppException("Not enough teams in the league.", HttpStatus.NOT_FOUND);
            }
        }
    }

    // This is a helper method to clear the draft order everytime it is randomized.
    private void clearOrder(String leagueName) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new AppException("Unknown league", HttpStatus.NOT_FOUND));

        league.getDraftOrder().clear();

        for (Team team : league.getTeams()) {
            team.getAllPicks().clear();
        }

        leagueRepository.save(league);
    }

    // The purpose of this method is to randomize the draft order. It randomizes the first
    // round pick for each team (based on the number of teams in each league. The rest of the
    // picks in the draft are assigned in the style of a snake draft, based on the first round
    // pick the team has.
    @Transactional
    public Map<Integer, Team> randomize(String leagueName) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new AppException("Unknown league", HttpStatus.NOT_FOUND));

        int numTeams = league.getNumTeams();

        ArrayList<Integer> draftPicks = new ArrayList<>();

        for (int i = 1; i <= numTeams; i++) {
            draftPicks.add(i);
        }

        Collections.shuffle(draftPicks, new Random(System.currentTimeMillis()));

        clearOrder(leagueName);
        int totalPicks = league.getRosterSize() * numTeams;

        int count = 0;
        for (Team team : league.getTeams()) {
            if (team.getTeamName() == null) {
                System.out.println("null team");
                throw new AppException("Team name cannot be null", HttpStatus.BAD_REQUEST);
            }
            team.setFirstRoundPick(draftPicks.get(count));
            team.setSecondRoundPick((numTeams * 2) - draftPicks.get(count) + 1);
            league.getDraftOrder().put(draftPicks.get(count), team);
            league.getDraftOrder().put((numTeams * 2) - draftPicks.get(count) + 1, team);
            team.getAllPicks().add(draftPicks.get(count));
            team.getAllPicks().add((numTeams * 2) - draftPicks.get(count) + 1);
            int prevOne = team.getFirstRoundPick();
            int prevTwo = team.getSecondRoundPick();

            // This loop sets all the odd round picks for the team.
            while (prevOne + (numTeams * 2) <= totalPicks) {
                prevOne = prevOne + (numTeams * 2);
                team.getAllPicks().add(prevOne);
                league.getDraftOrder().put(prevOne, team);
            }

            // This loop sets all the even round picks for the team.
            while (prevTwo + (numTeams * 2) <= totalPicks) {
                prevTwo = prevTwo + (numTeams * 2);
                team.getAllPicks().add(prevTwo);
                league.getDraftOrder().put(prevTwo, team);
            }

            count++;

            teamRepository.save(team);
        }

        leagueRepository.save(league);
        return league.getDraftOrder();
    }

    // This method checks if the draft for every league has started, based on the draft date.
    // If the draft is ready to start, the startDraft method is called.
    public void checkDraftStarts() {
        System.out.print("hello workinggg??");
        LocalDateTime currentDate = LocalDateTime.now();
        List<League> leagues = leagueRepository.findAll();

        for (League league : leagues) {
            if (league.getDraftDone() != null && league.getDraftStart() != null &&
                    !league.getDraftDone() && !league.getDraftStart()) {
                Date draftDate = league.getDraftDate();
                LocalDateTime draftDateTime = convertToLocalDateTimeViaInstant(draftDate);

                if (draftDateTime.isEqual(currentDate) || draftDateTime.isBefore(currentDate)) {
                    System.out.println("draft start");
                    league.setDraftStart(true);
                    leagueRepository.save(league);
                    startDraft(league);
                }
            }
        }
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date draftDate) {
        return draftDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private void startDraft(League league) {
        System.out.println("draft order is here:: " + league.getDraftOrder());
        Team team = league.getDraftOrder().get(league.getCurrentPick());
        System.out.println("team is here: " + team.getTeamName());
        team.setIsDraftTurn(true);
        teamRepository.save(team);
    }

    public List<Player> draftPlayer(String leagueName, String teamName, String playerName) {
        Player player = playerRepository.findByFullName(playerName)
                .orElseThrow(() -> new AppException("Unknown player", HttpStatus.NOT_FOUND));
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new AppException("Unknown league", HttpStatus.NOT_FOUND));

        Team team = null;


        for (Team t : league.getTeams()) {
            if (t.getTeamName().equals(teamName)) {
                team = t;
            }
        }

        team.getTeamPlayers().add(player);

        league.getTakenPlayers().add(player);
        league.getAvailablePlayers().remove(player);

        player.getAvailableLeagues().remove(league);
        player.getTakenLeagues().add(league);

        team.setIsDraftTurn(false);

        league.getDraftOrder().remove(league.getCurrentPick());
        league.setCurrentPick(league.getCurrentPick() + 1);

        leagueRepository.save(league);
        teamRepository.save(team);

        if (!league.getDraftOrder().isEmpty()) {
            startDraft(league);
        }

        for (Team t : league.getTeams()) {
            removeQueue(leagueName, t.getTeamName(), playerName);
        }

        return league.getAvailablePlayers();
    }

    public List<Player> queuePlayer(String leagueName, String teamName, String playerName) {
        Player player = playerRepository.findByFullName(playerName)
                .orElseThrow(() -> new AppException("Unknown player", HttpStatus.NOT_FOUND));
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new AppException("Unknown league", HttpStatus.NOT_FOUND));

        Team team = null;

        for (Team t : league.getTeams()) {
            if (t.getTeamName().equals(teamName)) {
                team = t;
            }
        }

        assert team != null;
        if (!team.getQueueList().contains(player)) {
            team.getQueueList().add(player);
            player.getQueueTeams().add(team);
        }

        teamRepository.save(team);

        return team.getQueueList();
    }

    public List<Player> removeQueue(String leagueName, String teamName, String playerName) {
        Player player = playerRepository.findByFullName(playerName)
                .orElseThrow(() -> new AppException("Unknown player", HttpStatus.NOT_FOUND));
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new AppException("Unknown league", HttpStatus.NOT_FOUND));

        Team team = null;

        for (Team t : league.getTeams()) {
            if (t.getTeamName().equals(teamName)) {
                team = t;
            }
        }

        assert team != null;

        if (team.getQueueList().contains(player)) {
            team.getQueueList().remove(player);
            player.getQueueTeams().remove(team);

            teamRepository.save(team);
        }

        return team.getQueueList();
    }
}

// https://prod.liveshare.vsengsaas.visualstudio.com/join?F7012F9D5927F9874EF447352AAA54FA2397
