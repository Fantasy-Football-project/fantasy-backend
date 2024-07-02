package com.sathvik.services;

import com.sathvik.dto.CreateLeagueDto;
import com.sathvik.dto.UserDto;
import com.sathvik.entities.League;
import com.sathvik.entities.User;
import com.sathvik.exceptions.AppException;
import com.sathvik.repositories.LeagueRepository;
import com.sathvik.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LeagueService {
    private final LeagueRepository leagueRepository;
    private final UserService userService;
    private final UserRepository userRepository;

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

        User user = userRepository.findByLogin(league.getUsername())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        user.getLeagues().add(newLeague);
        User saved = userRepository.save(user);
        //userRepository.save(user);
        newLeague.getUsers().add(saved);

        //userRepository.save(user);
        leagueRepository.save(newLeague);

        return newLeague;
    }

    public League getLeagueById(Long id) {
        return leagueRepository.findById(id)
                .orElse(null);
    }

    public List<League> getAllLeagues(Long userId) {
        // return array list of all leagues
        System.out.println(leagueRepository.findByUsers_Id(userId) + "hellooooo");

        return leagueRepository.findByUsers_Id(userId); // this works?
        //return leagueRepository.findAll();
    }
}
