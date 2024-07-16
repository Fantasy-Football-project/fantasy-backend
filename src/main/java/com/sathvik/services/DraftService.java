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

        int count = 0;
        for (Team team : league.getTeams()) {
            if (team.getTeamName() == null) {
                System.out.println("null team");
                throw new AppException("Team name cannot be null", HttpStatus.BAD_REQUEST);
            }
            team.setFirstRoundPick(draftPicks.get(count));
            league.getDraftOrder().put(draftPicks.get(count), team);
            count++;

            teamRepository.save(team);
        }

        leagueRepository.save(league);
        return league.getDraftOrder();
    }
}
