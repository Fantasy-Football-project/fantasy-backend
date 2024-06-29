package com.sathvik.services;

import com.sathvik.entities.League;
import com.sathvik.repositories.LeagueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LeagueService {
    private final LeagueRepository leagueRepository;

    public League createLeague(League league) {
        return leagueRepository.save(league);
    }

    public League getLeagueById(Long id) {
        return leagueRepository.findById(id)
                .orElse(null);
    }
}
