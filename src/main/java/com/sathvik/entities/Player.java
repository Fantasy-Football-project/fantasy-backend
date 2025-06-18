package com.sathvik.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "players")
public class Player {
    @Id
    private long id;

    @Column(name = "full_name")
    private String fullName;

    //Maybe need transient notation on this and takenLeagues?
    @ManyToMany(mappedBy = "availablePlayers")
    @JsonIgnore
    private Set<League> availableLeagues = new HashSet<>();

    @ManyToMany(mappedBy = "takenPlayers")
    @JsonIgnore
    private Set<League> takenLeagues = new HashSet<>();

    @ManyToMany(mappedBy = "queueList")
    @JsonIgnore
    private List<Team> queueTeams = new ArrayList<>();

    //could make an enum
    @Transient
    private String NFLTeam;

    //could make an enum
    @Column(name = "position")
    private String position;

    //has to be dependent on scoring system, data scraping
    @Transient
    private int averagePoints;
    @Transient
    private int currentPoints; //for the week

    @ElementCollection
    @Transient
    private ArrayList<Integer> pastScores;
    //gives error not exactly sure. might need an entity for scores - not anymore

    //Not sure we need this.
    /*@ManyToMany(mappedBy = "teamPlayers")
    @Transient
    private List<Team> fantasyTeams;*/

    @Override
    public String toString() {
        return fullName + " " + position;
    }
}
