package com.sathvik.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    //FetchType.EAGER is bad practice. Be careful with it, think about removing.
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "draft_pick_assigning",
            joinColumns = {@JoinColumn(name = "league_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "team_id", referencedColumnName = "id")}
    )
    @MapKeyColumn(name = "draft_position")
    private Map<Integer, Team> draftOrder = new HashMap<>();

    private Date draftDate;

    private Integer currentPick;

    private Boolean draftStart;

    private Boolean draftDone;

    @OneToMany(
            mappedBy = "league",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            orphanRemoval = true
    )
    //@JsonIgnore //IDK IF IM ALLOWED TO REMOVE THIS BUT I AM FOR NOW
    private List<Team> teams = new ArrayList<>();

    private int numTeams;

    @ManyToMany(mappedBy = "leagues")
    private List<User> users = new ArrayList<>();

    //Can we keep public?
    public enum Position {
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

    //Let's worry about this later.
    @ElementCollection
    private Map<Position, Integer> maxForPosition = new HashMap<>();

    @ElementCollection
    private Map<Position, Integer> numberOfStarters = new HashMap<>();

    private boolean ppr;
    private boolean nonPPR;
    private boolean halfPPR;

    //INSERT SCORING LATER

    private int playoffTeams;
    private int playoffGameLength;
    private int regularSeasonGames;

    private Integer currentWeekNumber = 1;

    // This field is for the schedule of the league. The integer represents the week,
    // and the WeekMatchups represents all the matches for that week.
    @OneToMany(cascade = CascadeType.ALL) //.ALL can be used on the one to many side, as deleting this field
    // would remove all the matches for the league which is fine.
    @JoinTable(
            name = "league_matchups",
            joinColumns = {@JoinColumn(name = "league_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "week_matchup_id", referencedColumnName = "id")}
    )
    @MapKeyColumn(name = "week_number")
    private Map<Integer, WeekMatchups> matchups = new HashMap<>();

    @OneToMany
    @JoinTable(
            name = "leagues_trade_requests",
            joinColumns = {@JoinColumn(name = "league_id")},
            inverseJoinColumns = {@JoinColumn(name = "trade_request_id")}
    )
    private Set<TradeRequest> tradeRequests = new HashSet<>();

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

    @ElementCollection
    private List<String> recentActivity = new ArrayList<>();

    @Override
    public String toString() {
        return "league";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        else {
            League other = (League) obj;
            return id == other.id;
        }
    }
}
