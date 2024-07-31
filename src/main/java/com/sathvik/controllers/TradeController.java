package com.sathvik.controllers;

import com.sathvik.entities.Player;
import com.sathvik.entities.TradeRequest;
import com.sathvik.services.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TradeController {

    private final TradeService tradeService;

    @PutMapping("/submit-trade-request")
    public ResponseEntity<List<Player>> submitTradeRequest(@RequestParam String leagueName,
                                   @RequestParam Long teamOneId, @RequestParam Long teamTwoId,
                                   @RequestParam List<Long> fromTeamOne,
                                   @RequestParam List<Long> fromTeamTwo) {
        return ResponseEntity.ok().
                body(tradeService.requestTrade(leagueName, teamOneId, teamTwoId, fromTeamOne, fromTeamTwo));
    }

    @GetMapping("/view-outgoing-requests")
    public ResponseEntity<List<TradeRequest>> viewOutgoingRequests(@RequestParam String leagueName,
                                                                   @RequestParam Long teamId) {
        return ResponseEntity.ok().body(tradeService.getOutgoingTrades(leagueName, teamId));
    }

    @GetMapping("/view-incoming-requests")
    public ResponseEntity<List<TradeRequest>> viewIncomingRequests(@RequestParam String leagueName,
                                                                   @RequestParam Long teamId) {
        return ResponseEntity.ok().body(tradeService.getIncomingTrades(leagueName, teamId));
    }

    @PutMapping("/accept-trade")
    public void acceptTrade(@RequestParam String leagueName,
                                                    @RequestParam Long tradeRequestId) {
        tradeService.acceptTrade(leagueName, tradeRequestId);
    }

    @PutMapping("/cancel-trade-request")
    public void cancelTradeRequest(@RequestParam String leagueName, @RequestParam Long tradeRequestId) {
        tradeService.cancelTradeRequest(leagueName, tradeRequestId);
    }
}
