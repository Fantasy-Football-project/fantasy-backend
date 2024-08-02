package com.sathvik.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "week_matchups")
public class WeekMatchups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinTable(
            name = "teams_list_a",
            joinColumns = {@JoinColumn(name = "week_matchup_id")},
            inverseJoinColumns = {@JoinColumn(name = "team_id")}
    )
    @JsonIgnore
    private List<Team> teamsListA = new ArrayList<>();

    @OneToMany
    @JoinTable(
            name = "teams_list_b",
            joinColumns = {@JoinColumn(name = "week_matchup_id")},
            inverseJoinColumns = {@JoinColumn(name = "team_id")}
    )
    @JsonIgnore
    private List<Team> teamsListB = new ArrayList<>();
}
