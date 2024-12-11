package com.example.Player.service;

import com.example.Player.model.PlayerYT;
import com.example.Player.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerYTService {

    private final PlayerRepository playerRepository;

    public PlayerYTService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public PlayerYT addPlayer(PlayerYT player) {
        return playerRepository.save(player);
    }


    public void deletePlayer(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new IllegalArgumentException("Player not found.");
        }

        playerRepository.deleteById(id);
    }

    public List<PlayerYT> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Optional<PlayerYT> getPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    public Optional<PlayerYT> getPlayerByLink(String link) {
        return playerRepository.findByLinkyt(link);
    }

    public PlayerYT likePlayer(Long id, String username) {
        PlayerYT player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Player not found."));

        if (player.getLike().contains(username)) {
            player.getLike().remove(username);
        } else {
            player.getLike().add(username);
            player.getUnlike().remove(username);
        }

        player.setCountlike(player.getLike().size());
        player.setCountunlike(player.getUnlike().size());

        return playerRepository.save(player);
    }

    public PlayerYT unlikePlayer(Long id, String username) {
        PlayerYT player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Player not found."));

        if (player.getUnlike().contains(username)) {
            player.getUnlike().remove(username);
        } else {
            player.getUnlike().add(username);
            player.getLike().remove(username);
        }

        player.setCountlike(player.getLike().size());
        player.setCountunlike(player.getUnlike().size());

        return playerRepository.save(player);
    }
}
