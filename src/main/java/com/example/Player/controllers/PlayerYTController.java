package com.example.Player.controllers;

import com.example.Player.model.PlayerYT;
import com.example.Player.service.PlayerYTService;
import com.example.Player.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/player")
public class PlayerYTController {

    private final PlayerYTService playerYTService;

    public PlayerYTController(PlayerYTService playerYTService) {
        this.playerYTService = playerYTService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPlayerYT(@RequestBody PlayerYT playerRequest) {
        try {
            Optional<PlayerYT> existingPlayer = playerYTService.getPlayerByLink(playerRequest.getLinkyt());
            if (existingPlayer.isPresent()) {
                return ResponseEntity.badRequest().body(
                        Map.of("status", "error", "message", "Player already exists")
                );
            }

            playerRequest.setLike(List.of());
            playerRequest.setUnlike(List.of());
            playerRequest.setCountlike(0);
            playerRequest.setCountunlike(0);

            PlayerYT savedPlayer = playerYTService.addPlayer(playerRequest);

            return ResponseEntity.ok(
                    Map.of("status", "success", "message", "Player added successfully")
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", e.getMessage())
            );
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updatePlayerYT(@PathVariable Long id, @RequestBody PlayerYT playerRequest) {
        try {
            Optional<PlayerYT> playerOptional = playerYTService.getPlayerById(id);
            if (playerOptional.isEmpty()) {
                return ResponseEntity.status(404).body(
                        Map.of("status", "error", "message", "Player not found")
                );
            }

            PlayerYT player = playerOptional.get();
            player.setLinkyt(playerRequest.getLinkyt());
            player.setCategory(playerRequest.getCategory());

            PlayerYT updatedPlayer = playerYTService.addPlayer(player);

            return ResponseEntity.ok(
                    Map.of("status", "success", "message", "Player updated successfully", "data", updatedPlayer)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", e.getMessage())
            );
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePlayerYT(@PathVariable Long id) {
        try {
            if (!playerYTService.getPlayerById(id).isPresent()) {
                return ResponseEntity.status(404).body(
                        Map.of("status", "error", "message", "Player not found")
                );
            }

            playerYTService.deletePlayer(id);

            return ResponseEntity.ok(
                    Map.of("status", "success", "message", "Player deleted successfully")
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", e.getMessage())
            );
        }
    }

    @GetMapping("/allPlayers")
    public ResponseEntity<?> getAllPlayersYT() {
        try {
            List<PlayerYT> players = playerYTService.getAllPlayers();
            return ResponseEntity.ok(Map.of("data", players));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", e.getMessage())
            );
        }
    }

    @GetMapping("getPlayer/{id}")
    public ResponseEntity<?> getPlayerYT(@PathVariable Long id) {
        try {
            Optional<PlayerYT> playerOptional = playerYTService.getPlayerById(id);
            if (playerOptional.isEmpty()) {
                return ResponseEntity.status(404).body(
                        Map.of("status", "error", "message", "Player not found")
                );
            }

            return ResponseEntity.ok(
                    Map.of("status", "success", "data", playerOptional.get())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", e.getMessage())
            );
        }
    }

    @PostMapping("/Like/{id}")
    public ResponseEntity<?> likePlayerYT(@PathVariable Long id, @RequestBody User user) {
        try {
            Optional<PlayerYT> playerOptional = playerYTService.getPlayerById(id);
            if (playerOptional.isEmpty()) {
                return ResponseEntity.status(404).body(
                        Map.of("status", "error", "message", "Player not found")
                );
            }

            PlayerYT player = playerOptional.get();
            String username = user.getLogin();

            playerYTService.likePlayer(id, username);

            PlayerYT updatedPlayer = playerYTService.addPlayer(player);

            return ResponseEntity.ok(
                    Map.of("status", "success", "message", "Player liked successfully", "player", updatedPlayer)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", e.getMessage())
            );
        }
    }

    @PostMapping("/Unlike/{id}")
    public ResponseEntity<?> unlikePlayerYT(@PathVariable Long id, @RequestBody User user) {
        try {
            Optional<PlayerYT> playerOptional = playerYTService.getPlayerById(id);
            if (playerOptional.isEmpty()) {
                return ResponseEntity.status(404).body(
                        Map.of("status", "error", "message", "Player not found")
                );
            }

            PlayerYT player = playerOptional.get();
            String username = user.getLogin();

            playerYTService.unlikePlayer(id, username);

            PlayerYT updatedPlayer = playerYTService.addPlayer(player);

            return ResponseEntity.ok(
                    Map.of("status", "success", "message", "Player unliked successfully", "player", updatedPlayer)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", e.getMessage())
            );
        }
    }
}
