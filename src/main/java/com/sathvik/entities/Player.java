package com.sathvik.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "full_name")
    private String fullName;

    @ManyToMany(mappedBy = "players")
    @Transient
    private HashSet<League> leagues;

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

    @ManyToMany(mappedBy = "teamPlayers")
    @Transient
    private List<Team> fantasyTeams;

    @Override
    public String toString() {
        return fullName + " " + position;
    }
}
