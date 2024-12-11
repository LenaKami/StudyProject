package com.example.Player.repository;


import com.example.Player.model.PlayerYT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<PlayerYT, Long> {
    Optional<PlayerYT> findByLinkyt(String linkyt);
}
