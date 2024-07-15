package com.sathvik.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "past_data")
public class PastPlayerData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dst_team")
    private String teamName;

    @Column(name = "defensive_stats_sacks")
    private String defensive_stats_sacks;

    @Column(name = "defensive_stats_interceptions")
    private String defensive_stats_interceptions;

    @Column(name = "defensive_stats_fumble_recoveries")
    private String defensive_stats_fumble_recoveries;

    @Column(name = "defensive_stats_forced_fumbles")
    private String defensive_stats_forced_fumbles;

    @Column(name = "defensive_stats_defensive_touchdowns")
    private String defensive_stats_defensive_touchdowns;

    @Column(name = "defensive_stats_safeties")
    private String defensive_stats_safeties;

    @Column(name = "defensive_stats_special_team_touchdowns")
    private String defensive_stats_special_team_touchdowns;

    @Column(name = "defensive_stats_games")
    private String defensive_stats_games;

    @Column(name = "defensive_stats_fpts")
    private String defensive_stats_fpts;

    @Column(name = "defensive_stats_fpts_per_game")
    private String defensive_stats_fpts_per_game;

    @Column(name = "defensive_stats_rostered_percentage")
    private String defensive_stats_rostered_percentage;

    @Column(name = "passing_stats_season")
    private String passing_stats_season;

    @Column(name = "passing_stats_team")
    private String passing_stats_team;

    @Column(name = "passing_stats_games")
    private String passing_stats_games;

    @Column(name = "passing_stats_QB_rating")
    private String passing_stats_QB_rating;

    @Column(name = "passing_stats_completions")
    private String passing_stats_completions;

    @Column(name = "passing_stats_attempts")
    private String passing_stats_attempts;

    @Column(name = "passing_stats_completion_percentage")
    private String passing_stats_completion_percentage;

    @Column(name = "passing_stats_yards")
    private String passing_stats_yards;

    @Column(name = "passing_stats_yards_per_attempt")
    private String passing_stats_yards_per_attempt;

    @Column(name = "passing_stats_touchdowns")
    private String passing_stats_touchdowns;

    @Column(name = "passing_stats_interceptions")
    private String passing_stats_interceptions;

    @Column(name = "passing_stats_sacks")
    private String passing_stats_sacks;

    @Column(name = "player_name")
    private String playerName;

    @Column(name = "position")
    private String position;

    @Column(name = "height")
    private String height;

    @Column(name = "weight")
    private String weight;

    @Column(name = "age")
    private String age;

    @Column(name = "rushing_stats_season")
    private String rushing_stats_season;

    @Column(name = "rushing_stats_team")
    private String rushing_stats_team;

    @Column(name = "rushing_stats_games")
    private String rushing_stats_games;

    @Column(name = "rushing_stats_attempts")
    private String rushing_stats_attempts;

    @Column(name = "rushing_stats_yards")
    private String rushing_stats_yards;

    @Column(name = "rushing_stats_yards_per_attempt")
    private String rushing_stats_yards_per_attempt;

    @Column(name = "rushing_stats_longest_gain")
    private String rushing_stats_longest_gain;

    @Column(name = "rushing_stats_touchdowns")
    private String rushing_stats_touchdowns;

    @Column(name = "rushing_stats_fumbles")
    private String rushing_stats_fumbles;

    @Column(name = "rushing_stats_fumbles_lost")
    private String rushing_stats_fumbles_lost;

    @Column(name = "receiving_stats_season")
    private String receiving_stats_season;

    @Column(name = "receiving_stats_team")
    private String receiving_stats_team;

    @Column(name = "receiving_stats_games")
    private String receiving_stats_games;

    @Column(name = "receiving_stats_receptions")
    private String receiving_stats_receptions;

    @Column(name = "receiving_stats_targets")
    private String receiving_stats_targets;

    @Column(name = "receiving_stats_yards")
    private String receiving_stats_yards;

    @Column(name = "receiving_stats_yards_per_reception")
    private String receiving_stats_yards_per_reception;

    @Column(name = "receiving_stats_longest_gain")
    private String receiving_stats_longest_gain;

    @Column(name = "receiving_stats_touchdowns")
    private String receiving_stats_touchdowns;

    @Column(name = "kicking_stats_team")
    private String kicking_stats_team;

    @Column(name = "kicking_stats_games")
    private String kicking_stats_games;

    @Column(name = "kicking_stats_fgs")
    private String kicking_stats_fgs;

    @Column(name = "kicking_stats_fga")
    private String kicking_stats_fga;

    @Column(name = "kicking_stats_pats")
    private String kicking_stats_pats;

    @Column(name = "kicking_stats_season")
    private String kicking_stats_season;
}