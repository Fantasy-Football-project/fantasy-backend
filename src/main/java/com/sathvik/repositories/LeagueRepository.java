package com.sathvik.repositories;

import com.sathvik.entities.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LeagueRepository extends JpaRepository<League, Long> {

    List<League> findByUsers_Id(Long id);


    Optional<League> findByLeagueName(String leagueName);
    Optional<League> findByJoinCode(String joinCode);
}
