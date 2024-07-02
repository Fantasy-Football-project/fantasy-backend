package com.sathvik.repositories;

import com.sathvik.entities.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeagueRepository extends JpaRepository<League, Long> {

    List<League> findByUsers_Id(Long id);
}
