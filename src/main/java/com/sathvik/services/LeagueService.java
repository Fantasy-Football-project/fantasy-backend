package com.sathvik.services;

import com.sathvik.dto.CreateLeagueDto;
import com.sathvik.entities.League;
import com.sathvik.repositories.LeagueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LeagueService {
    private final LeagueRepository leagueRepository;

    /*
    This method has a CreateLeagueDto object as a parameter, which contains the
    basic information needed to create a league. The league is saved into the database.
     */
    public League createLeague(CreateLeagueDto league) {
        League newLeague = new League();
        newLeague.setLeagueName(league.getLeagueName());
        newLeague.setNumTeams(league.getNumTeams());
        newLeague.setPpr(league.getPpr());
        newLeague.setNonPPR(league.getNonPPR());
        newLeague.setHalfPPR(league.getHalfPPR());


        return leagueRepository.save(newLeague);
    }

    public League getLeagueById(Long id) {
        return leagueRepository.findById(id)
                .orElse(null);
    }

    public List<League> getAllLeagues(Long userId) {
        // return array list of all leagues
        System.out.println(leagueRepository.findLeaguesByUserId(userId) + "hellooooo");
        return leagueRepository.findLeaguesByUserId(userId); // this works?
    }
}
