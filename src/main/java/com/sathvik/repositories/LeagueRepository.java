package com.sathvik.repositories;

import com.sathvik.entities.League;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeagueRepository extends JpaRepository<League, Long> {
    // Derived query method to find leagues by user ID
    List<League> findByUsers_Id(Long userId);
}
