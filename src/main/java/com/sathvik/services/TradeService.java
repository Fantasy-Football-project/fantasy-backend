package com.sathvik.services;

import com.sathvik.entities.League;
import com.sathvik.entities.Player;
import com.sathvik.entities.Team;
import com.sathvik.entities.TradeRequest;
import com.sathvik.repositories.LeagueRepository;
import com.sathvik.repositories.PlayerRepository;
import com.sathvik.repositories.TeamRepository;
import com.sathvik.repositories.TradeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TradeService {
    private final LeagueRepository leagueRepository;
    private final PlayerRepository playerRepository;
    private final AddPlayerService addPlayerService;
    private final TeamRepository teamRepository;
    private final TradeRequestRepository tradeRequestRepository;

    public List<Player> requestTrade(String leagueName,
                                    Long teamOneId, Long teamTwoId,
                                    List<Long> fromTeamOne, List<Long> fromTeamTwo) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new IllegalArgumentException("League not found"));
        Team teamOne = teamRepository.findById(teamOneId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        Team teamTwo = teamRepository.findById(teamTwoId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        List<Player> playersFromTeamOne = new ArrayList<>();
        List<Player> playersFromTeamTwo = new ArrayList<>();


        for (Long l : fromTeamOne) {
            Player player = playerRepository.findById(l)
                    .orElseThrow(() -> new IllegalArgumentException("Player not found"));

            playersFromTeamOne.add(player);
        }

        for (Long l : fromTeamTwo) {
            Player player = playerRepository.findById(l)
                    .orElseThrow(() -> new IllegalArgumentException("Player not found"));

            playersFromTeamTwo.add(player);
        }

        TradeRequest newTradeRequest = new TradeRequest();

        newTradeRequest.setTeamRequesting(teamOne);
        newTradeRequest.setTeamPending(teamTwo);
        newTradeRequest.setFromTeamRequesting(playersFromTeamOne);
        newTradeRequest.setFromTeamPending(playersFromTeamTwo);

        league.getTradeRequests().add(newTradeRequest);

        tradeRequestRepository.save(newTradeRequest);
        leagueRepository.save(league);

        return playersFromTeamOne;
    }

    public void acceptTrade(String leagueName, Long tradeRequestId) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new IllegalArgumentException("League not found"));
        TradeRequest req = tradeRequestRepository.findById(tradeRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Trade request not found"));
        Team teamOne = req.getTeamRequesting();
        Team teamTwo = req.getTeamPending();
        List<Player> playersFromTeamOne = req.getFromTeamRequesting();
        List<Player> playersFromTeamTwo = req.getFromTeamPending();
        StringBuilder activity = new StringBuilder("TRADE: From " + teamOne.getTeamName() + ": ");

        for (Player player : playersFromTeamOne) {
            addPlayerService.dropPlayer(leagueName, teamOne.getUser().getLogin(), player.getId(), true);
            activity.append(player.getFullName()).append(", ");
        }

        activity.append("\n From ").append(teamTwo.getTeamName()).append(": ");
        for (Player player : playersFromTeamTwo) {
            addPlayerService.dropPlayer(leagueName, teamTwo.getUser().getLogin(), player.getId(), true);
            activity.append(player.getFullName()).append(", ");
        }

        for (Player player : playersFromTeamOne) {
            addPlayerService.addPlayer(leagueName, teamTwo.getUser().getLogin(), player.getId(), true);
        }

        for (Player player : playersFromTeamTwo) {
            addPlayerService.addPlayer(leagueName, teamOne.getUser().getLogin(), player.getId(), true);
        }

        league.getRecentActivity().add(activity.toString());

        leagueRepository.save(league);
        teamRepository.save(teamOne);
    }

    public List<TradeRequest> getOutgoingTrades(String leagueName, Long teamId) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new IllegalArgumentException("League not found"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        List<TradeRequest> outgoingRequests = new ArrayList<>();

        for (TradeRequest req : league.getTradeRequests()) {
            if (req.getTeamRequesting().equals(team)) {
                outgoingRequests.add(req);
            }
        }

        return outgoingRequests;
    }

    public List<TradeRequest> getIncomingTrades(String leagueName, Long teamId) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new IllegalArgumentException("League not found"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));
        List<TradeRequest> incomingRequests = new ArrayList<>();

        for (TradeRequest req : league.getTradeRequests()) {
            if (req.getTeamPending().equals(team)) {
                incomingRequests.add(req);
            }
        }

        return incomingRequests;
    }

    public void cancelTradeRequest(String leagueName, Long tradeRequestId) {
        League league = leagueRepository.findByLeagueName(leagueName)
                .orElseThrow(() -> new IllegalArgumentException("League not found"));
        Optional<TradeRequest> req = tradeRequestRepository.findById(tradeRequestId);

        if (req.isPresent()) {
            league.getTradeRequests().remove(req.get());
            tradeRequestRepository.delete(req.get());
        }

        leagueRepository.save(league);
    }
}
