package com.sathvik.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.HashSet;

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
            name = "league_players",
            joinColumns = @JoinColumn(name = "league_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private HashSet<Player> players;

    @OneToMany(
            mappedBy = "league",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private ArrayList<Team> teams;
}
