package com.sathvik.services;

import com.sathvik.entities.League;
import com.sathvik.entities.Team;
import com.sathvik.entities.WeekMatchups;
import com.sathvik.repositories.LeagueRepository;
import com.sathvik.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public void randomizeSchedule(String leagueName) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new RuntimeException("League not found"));

        league.getMatchups().clear();

        int regularSeasonMatches = league.getRegularSeasonGames();
        List<Team> teams = league.getTeams();
        List<Integer> toBeShuffled = new ArrayList<>();

        for (int i = 0; i < league.getTeams().size(); i++) {
            toBeShuffled.add(i);
        }

        for (Team team : teams) {
            team.getOpponent().clear();
            teamRepository.save(team);
        }

        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < regularSeasonMatches; i++) {
            WeekMatchups week = new WeekMatchups();

            Collections.shuffle(toBeShuffled, random);

            for (int j = 0; j < toBeShuffled.size() / 2; j++) {
                week.getTeamsListA().add(teams.get(toBeShuffled.get(j)));
            }

            for (int j = toBeShuffled.size() / 2; j < toBeShuffled.size(); j++) {
                week.getTeamsListB().add(teams.get(toBeShuffled.get(j)));
            }

            league.getMatchups().put(i + 1, week);

            for (int j = 0; j < week.getTeamsListA().size(); j++) {
                week.getTeamsListA().get(j).getOpponent().put(i + 1, week.getTeamsListB().get(j));
                teamRepository.save(week.getTeamsListA().get(j));
            }

            for (int j = 0; j < week.getTeamsListB().size(); j++) {
                week.getTeamsListB().get(j).getOpponent().put(i + 1, week.getTeamsListA().get(j));
                teamRepository.save(week.getTeamsListB().get(j));
            }
        }

        leagueRepository.save(league);
    }

    public Map<Integer, WeekMatchups> getAllMatchups(String leagueName) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new RuntimeException("League not found"));

        return league.getMatchups();
    }

    // The purpose of this method is to return all the opponents of a team, since we used the
    // @JSONIgnore annotation to prevent infinite recursion.
    public Map<Integer, Team> getOpponents(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        return team.getOpponent();
    }
}
