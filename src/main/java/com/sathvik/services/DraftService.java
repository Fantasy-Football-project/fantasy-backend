package com.sathvik.services;

import com.sathvik.entities.League;
import com.sathvik.entities.Team;
import com.sathvik.exceptions.AppException;
import com.sathvik.repositories.LeagueRepository;
import com.sathvik.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class DraftService {
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;

    public void clearOrder(String leagueName) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new AppException("Unknown league", HttpStatus.NOT_FOUND));

        league.getDraftOrder().clear();

        for (Team team : league.getTeams()) {
            team.getAllPicks().clear();
        }

        leagueRepository.save(league);
    }


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
            team.getAllPicks().add(draftPicks.get(count));
            team.getAllPicks().add((numTeams * 2) - draftPicks.get(count) + 1);
            int prevOne = team.getFirstRoundPick();
            int prevTwo = team.getSecondRoundPick();

            while (prevOne + (numTeams * 2) <= totalPicks) {
                prevOne = prevOne + (numTeams * 2);
                team.getAllPicks().add(prevOne);
                league.getDraftOrder().put(prevOne, team);
            }

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
}
