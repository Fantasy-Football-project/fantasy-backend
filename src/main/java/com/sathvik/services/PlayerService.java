package com.sathvik.services;

import com.sathvik.entities.League;
import com.sathvik.entities.Player;
import com.sathvik.exceptions.AppException;
import com.sathvik.repositories.LeagueRepository;
import com.sathvik.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final LeagueRepository leagueRepository;

    public Page<Player> getAllPlayers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return playerRepository.findAll(pageable);
    }

    public Page<Player> getAllAvailablePlayers(String leagueName, int pageNumber, int pageSize) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new AppException("Unknown league", HttpStatus.NOT_FOUND));
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Player> availablePlayers = playerRepository.findAll();

        availablePlayers.removeIf(player -> league.getTakenPlayers().contains(player));

        return new PageImpl<>(availablePlayers, pageable, availablePlayers.size());
    }

    public Optional<Player> getPlayerById(Long playerId) {
        return playerRepository.findById(playerId);
    }
}
