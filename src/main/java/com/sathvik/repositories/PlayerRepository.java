package com.sathvik.repositories;

import com.sathvik.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByFullName(String fullName);

    Optional<Player> findById(long id);
}
