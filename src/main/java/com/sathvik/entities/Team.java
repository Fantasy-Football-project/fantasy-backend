package com.sathvik.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
//@Getter
//@Setter
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
    @JsonIgnore
    private League league;
    //might need another one but prolly not for waiverOrder

    @ManyToOne
    @JoinTable(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "team_players",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> teamPlayers;

    private Integer firstRoundPick;

    private Integer secondRoundPick = 0;

    private Boolean isDraftTurn = false;

    @ElementCollection
    private List<Integer> allPicks = new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "queue_list",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> queueList = new ArrayList<>();

    private Date mostRecentTransaction;

    private double pointsFor;
    private double pointsAgainst;

    private Boolean commissioner;

    @Override
    public String toString() {
        return "Team [id=" + id + ", teamName=" + teamName + ", fullName=" + fullName;
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
            Team other = (Team) obj;
            return id == other.id;
        }
    }

}
