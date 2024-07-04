package com.sathvik.services;

import com.sathvik.dto.CreateTeamDto;
import com.sathvik.entities.League;
import com.sathvik.entities.Player;
import com.sathvik.entities.Team;
import com.sathvik.entities.User;
import com.sathvik.exceptions.AppException;
import com.sathvik.repositories.TeamRepository;
import com.sathvik.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public Team findTeam(String leagueName, String username) {
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        League league = null;

        for (League l : user.getLeagues()) {
            if (l.getLeagueName().equals(leagueName)) {
                league = l;
            }
        }

        if (league != null) {
            for (Team t : league.getTeams()) {
                if (t.getUser().equals(user)) {
                    return t;
                }
            }
        }

        return null;
    }
}
