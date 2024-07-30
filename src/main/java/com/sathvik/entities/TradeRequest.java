package com.sathvik.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
@Entity
@Table(name = "trade_request")
public class TradeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "team_requesting_id")
    private Team teamRequesting;

    @OneToOne
    @JoinColumn(name = "team_pending_id")
    private Team teamPending;

    @OneToMany
    @JoinTable(
            name = "from_team_requesting",
            joinColumns = @JoinColumn(name = "trade_request_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> fromTeamRequesting = new ArrayList<>();

    @OneToMany
    @JoinTable(
            name = "from_team_pending",
            joinColumns = @JoinColumn(name = "trade_request_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> fromTeamPending = new ArrayList<>();
}
