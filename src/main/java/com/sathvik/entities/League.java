package com.sathvik.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.Name;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "leagues")
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String leagueName;

    @ManyToMany
    @JoinTable(
            name = "availableLeague_players",
            joinColumns = @JoinColumn(name = "league_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> availablePlayers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "takenLeague_players",
            joinColumns = @JoinColumn(name = "league_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> takenPlayers = new ArrayList<>();

    @OneToMany(
            mappedBy = "league",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Team> teams = new ArrayList<>();

    private int numTeams;

    @ManyToMany(mappedBy = "leagues")
    private List<User> users = new ArrayList<>();

    private enum Position {
        QB,
        RB,
        WR,
        TE,
        FLEX,
        DST,
        K,
        BE,
        IR
    }

    private int rosterSize;

    @ElementCollection
    private Map<Position, Integer> maxForPosition = new HashMap<>();

    @ElementCollection
    private Map<Position, Integer> numberOfStarters = new HashMap<>();

    private boolean ppr;
    private boolean nonPPR;
    private boolean halfPPR;

    //INSERT SCORING LATER

    //IMPLEMENT DRAFT SETTINGS

    private int playoffTeams;
    private int playoffGameLength;
    private int regularSeasonGames;

    private enum tradeDeadline {
        NOV25,
        DEC2,
        DEC9,
        DEC16,
        DEC23,
        DEC30,
        JAN5;
        /*private Date(final Date date){
            this.date = date;
        }
        private Date date;*/
    }

    private enum waiverSetting {
        STANDINGS,
        RECENT_TRANSACTION
    }

    private enum playerAcquisitionSystem {
        WAIVERS,
        INSTANT_ADD
    }

    private final int TRADE_REVIEW_PERIOD = 1;

    private String joinCode = UUID.randomUUID().toString();

    @Override
    public String toString() {
        return "league";
    }
}
