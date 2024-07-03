package com.sathvik.services;

import com.sathvik.dto.CreateTeamDto;
import com.sathvik.entities.Team;
import com.sathvik.entities.User;
import com.sathvik.exceptions.AppException;
import com.sathvik.repositories.TeamRepository;
import com.sathvik.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    /*
    The purpose of this method is to add a team to a league.
     */
    public Team addTeam(CreateTeamDto teamDto) {
        Team team = new Team();

        team.setTeamName(teamDto.getTeamName());
        team.setFullName(teamDto.getFullName());

        User user = userRepository.findByLogin(teamDto.getUsername())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        user.getTeams().add(team);
        User saved = userRepository.save(user);
        team.setUser(saved);

        teamRepository.save(team);

        return team;
    }
}
