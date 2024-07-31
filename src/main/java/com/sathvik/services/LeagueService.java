package com.sathvik.services;

import com.sathvik.dto.CreateLeagueDto;
import com.sathvik.dto.CreateTeamDto;
import com.sathvik.entities.League;
import com.sathvik.entities.Player;
import com.sathvik.entities.Team;
import com.sathvik.entities.User;
import com.sathvik.exceptions.AppException;
import com.sathvik.repositories.LeagueRepository;
import com.sathvik.repositories.PlayerRepository;
import com.sathvik.repositories.TeamRepository;
import com.sathvik.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LeagueService {
    private final LeagueRepository leagueRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    /*
    This method has a CreateLeagueDto object as a parameter, which contains the
    basic information needed to create a league. The league is saved into the database.
     */

    //NEED TO MAKE SURE TO DEFAULT OTHER VALUES LATER
    @Transactional
    public League createLeague(CreateLeagueDto league) {
        Optional<League> check = leagueRepository.findByLeagueName(league.getLeagueName());
        //Condition checks if the league already exists
        if (check.isPresent()) {
            throw new AppException("League already exists", HttpStatus.BAD_REQUEST);
        }

        League newLeague = new League();
        newLeague.setLeagueName(league.getLeagueName());
        newLeague.setNumTeams(league.getNumTeams());
        newLeague.setPpr(league.getPpr());
        newLeague.setNonPPR(league.getNonPPR());
        newLeague.setHalfPPR(league.getHalfPPR());
        newLeague.setAvailablePlayers(playerRepository.findAll());
        newLeague.setDraftStart(false);
        newLeague.setDraftDone(false);
        newLeague.setCurrentPick(1);
        newLeague.setRosterSize(16);

        //Adjusting the position limits for the league (default values).
        newLeague.getNumberOfStarters().put(League.Position.QB, 1);
        newLeague.getNumberOfStarters().put(League.Position.RB, 2);
        newLeague.getNumberOfStarters().put(League.Position.WR, 2);
        newLeague.getNumberOfStarters().put(League.Position.TE, 1);
        newLeague.getNumberOfStarters().put(League.Position.FLEX, 1);
        newLeague.getNumberOfStarters().put(League.Position.K, 1);
        newLeague.getNumberOfStarters().put(League.Position.DST, 1);
        newLeague.getNumberOfStarters().put(League.Position.BE, 7);

        User user = userRepository.findByLogin(league.getUsername())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        user.getLeagues().add(newLeague);
        User saved = userRepository.save(user);
        newLeague.getUsers().add(saved);

        //Because of the cascade annotation, I only have to save it once, and it'll also
        //save to the user.
        userRepository.save(saved);

        System.out.println("hereeee" + newLeague);

        //Logic for adding a new team to the league and user.
        CreateTeamDto team  = new CreateTeamDto();
        team.setTeamName(league.getTeamName());
        team.setFullName(league.getFullName());
        team.setUsername(league.getUsername());
        team.setLeagueName(league.getLeagueName());

        addTeam(team);

        return newLeague;
    }

    /*
    The purpose of this method is to add a team to a league.
     */
    public void addTeam(CreateTeamDto teamDto) {
        Team team = new Team();

        team.setTeamName(teamDto.getTeamName());
        team.setFullName(teamDto.getFullName());

        User user = userRepository.findByLogin(teamDto.getUsername())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        League league;
        if (teamDto.getLeagueName() != null) {
            league = leagueRepository.findByLeagueName(teamDto.getLeagueName())
                    .orElseThrow(() -> new AppException("Unknown league", HttpStatus.NOT_FOUND));
        }
        else {
            league = leagueRepository.findByJoinCode(teamDto.getJoinCode())
                    .orElseThrow(() -> new AppException("Unknown league", HttpStatus.NOT_FOUND));
        }

        if (league.getTeams().isEmpty()) {
            team.setCommissioner(true);
        }
        else {
            team.setCommissioner(false);
        }

        team.setUser(user);
        team.setLeague(league);
        team.setFirstRoundPick(0);
        league.getTeams().add(team);
        user.getTeams().add(team);
        teamRepository.save(team);
    }

    //Don't think this method is necessary but will leave here for now.
    public void joinLeague(CreateTeamDto teamDto) {
        User user = userRepository.findByLogin(teamDto.getUsername())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        League league = leagueRepository.findByJoinCode(teamDto.getJoinCode())
                .orElseThrow(() -> new AppException("Unknown league", HttpStatus.NOT_FOUND));

        user.getLeagues().add(league);
        User saved = userRepository.save(user);
        league.getUsers().add(saved);

        addTeam(teamDto);
    }

    public List<League> getLeagues(Long userId) {
        // return array list of all leagues
        System.out.println(leagueRepository.findByUsers_Id(userId) + "hellooooo");

        return leagueRepository.findByUsers_Id(userId); // this works?
        //return leagueRepository.findAll();
    }

    public League getLeague(String leagueName) {
        return leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new AppException("Unknown league", HttpStatus.NOT_FOUND));
    }

    public List<League> getAllLeagues() {
        // return array list of all leagues

        return leagueRepository.findAll(); // this works?
    }

    @Transactional
    public void deleteLeague(String leagueName) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new AppException("Unknown league", HttpStatus.NOT_FOUND));
        List<User> users = league.getUsers();

        for (User user : users) {
            user.getLeagues().remove(league);
            userRepository.save(user);
        }

        List<Team> teams = league.getTeams();

        for (Team team : teams) {
            User user = team.getUser();
            user.getTeams().remove(team);
            userRepository.save(user);
        }

        List<Player> players = playerRepository.findAll();

        for (Player player : players) {
            if (player.getAvailableLeagues() != null) {
                player.getAvailableLeagues().remove(league);
            }

            if (player.getTakenLeagues() != null) {
                player.getTakenLeagues().remove(league);
            }

            playerRepository.save(player);
        }

        teamRepository.deleteAll(teams);

        leagueRepository.delete(league);
    }

    public List<Player> getAllAvailablePlayers(String leagueName) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new AppException("Unknown league", HttpStatus.NOT_FOUND));

        return league.getAvailablePlayers();
    }

    // This method gets the recent activity of the league (including adds, drops, trades, etc).
    public List<String> getRecentActivity(String leagueName) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new AppException("Unknown league", HttpStatus.NOT_FOUND));

        return league.getRecentActivity();
    }
}
