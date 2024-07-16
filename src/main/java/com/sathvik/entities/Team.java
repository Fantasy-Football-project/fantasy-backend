package com.sathvik.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

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

    @Column(name = "first_round_pick", nullable = false)
    private Integer firstRoundPick;

    private Date mostRecentTransaction;

    private double pointsFor;
    private double pointsAgainst;

    @Column(name = "commissioner", nullable = false)
    private Boolean commissioner;

}
