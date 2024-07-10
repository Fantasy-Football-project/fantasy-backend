package com.sathvik.repositories;

import com.sathvik.entities.PastPlayerData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PastPlayerDataRepository extends JpaRepository<PastPlayerData, Long> {
    List<PastPlayerData> findByPlayerName(String playerName);
}
