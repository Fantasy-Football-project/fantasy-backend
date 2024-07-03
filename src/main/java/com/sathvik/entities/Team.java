package com.sathvik.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "fantasy_teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String teamName;

    private String fullName;

    @ManyToOne
    @JoinTable(name = "league_id")
    private League league;
    //might need another one but prolly not for waiverOrder

    @ManyToOne
    @JoinTable(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "team_players",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> teamPlayers;

    private Date mostRecentTransaction;

    private double pointsFor;
    private double pointsAgainst;
}
