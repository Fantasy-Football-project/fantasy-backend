package com.sathvik.services;

import com.sathvik.entities.League;
import com.sathvik.repositories.LeagueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LeagueSettingService {
    private final LeagueRepository leagueRepository;

    public League adjustRosterSize(String leagueName, int rosterSize) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new RuntimeException("League not found"));

        league.setRosterSize(rosterSize);
        leagueRepository.save(league);
        return league;
    }
}
