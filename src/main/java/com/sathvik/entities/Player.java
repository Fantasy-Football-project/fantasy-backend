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
@Table(name = "football_players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fullName;

    @ManyToMany(mappedBy = "players")
    private HashSet<League> leagues;

    //could make an enum
    private String NFLTeam;

    //could make an enum
    private String position;

    //has to be dependent on scoring system, data scraping
    private int averagePoints;
    private int currentPoints; //for the week

    @ElementCollection
    private ArrayList<Integer> pastScores;
    //gives error not exactly sure. might need an entity for scores - not anymore

    @ManyToMany(mappedBy = "teamPlayers")
    private List<Team> fantasyTeams;
}
