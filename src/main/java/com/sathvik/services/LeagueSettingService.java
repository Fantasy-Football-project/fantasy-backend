package com.sathvik.services;

import com.sathvik.entities.League;
import com.sathvik.repositories.LeagueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LeagueSettingService {
    private final LeagueRepository leagueRepository;
}
