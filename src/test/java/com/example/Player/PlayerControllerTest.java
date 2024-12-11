package com.example.Player;

import com.example.Player.model.PlayerYT;
import com.example.Player.model.User;
import com.example.Player.service.PlayerYTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerYTService playerYTService;

    private String authToken;
    @BeforeEach
    void setUp() throws Exception {
        authToken = mockMvc.perform(post("/user/login")
                        .contentType("application/json")
                        .content("{\"login\":\"testuser\",\"password\":\"password123\"}"))
                        //.content("{\"login\":\"Karma123\",\"password\":\"Karma123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andReturn().getResponse().getContentAsString();
        authToken = authToken.split("\"token\":\"")[1].split("\"")[0];
    }


    @Test
    void shouldUpdatePlayerSuccessfully() throws Exception {
        PlayerYT player = new PlayerYT();
        player.setLinkyt("https://youtube.com/somechannel");
        player.setCategory("Test");
        PlayerYT savedPlayer = playerYTService.addPlayer(player);

        // Aktualizacja kategorii gracza
        mockMvc.perform(put("/player/update/{id}", savedPlayer.getId())
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + authToken)
                        .content("{\"linkyt\":\"https://youtube.com/somechannel\",\"category\":\"Music\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Player updated successfully"))
                .andExpect(jsonPath("$.data.category").value("Music"));
    }

    @Test
    void shouldNotUpdatePlayerNotFound() throws Exception {
        mockMvc.perform(put("/player/update/{id}", 999L)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + authToken)
                        .content("{\"linkyt\":\"https://youtube.com/somechannel\",\"category\":\"Music\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Player not found"));
    }

    @Test
    void shouldDeletePlayerSuccessfully() throws Exception {
        PlayerYT player = new PlayerYT();
        player.setLinkyt("https://youtube.com/somechannel");
        player.setCategory("Test");
        PlayerYT savedPlayer = playerYTService.addPlayer(player);

        mockMvc.perform(delete("/player/delete/{id}", savedPlayer.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Player deleted successfully"));
    }

    @Test
    void shouldGetAllPlayers() throws Exception {
        mockMvc.perform(get("/player/allPlayers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void shouldGetPlayerByIdSuccessfully() throws Exception {
        PlayerYT player = new PlayerYT();
        player.setLinkyt("https://youtube.com/somechannel");
        player.setCategory("Test");
        PlayerYT savedPlayer = playerYTService.addPlayer(player);

        mockMvc.perform(get("/player/getPlayer/{id}", savedPlayer.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.linkyt").value("https://youtube.com/somechannel"));
    }

    @Test
    void shouldNotGetPlayerByIdNotFound() throws Exception {
        mockMvc.perform(get("/player/getPlayer/{id}", 999L)
                        .header("Authorization", "Bearer " + authToken) )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Player not found"));
    }

    @Test
    void shouldLikePlayerSuccessfully() throws Exception {
        PlayerYT player = new PlayerYT();
        player.setLinkyt("https://youtube.com/somechannel1111");
        player.setCategory("Test");
        PlayerYT savedPlayer = playerYTService.addPlayer(player);

        mockMvc.perform(post("/player/Like/{id}", savedPlayer.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType("application/json")
                        .content("{\"login\":\"Karma123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Player liked successfully"));
    }

    @Test
    void shouldLikePlayerByNotFound() throws Exception {
        mockMvc.perform(post("/player/Like/{id}", 999L)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType("application/json")
                        .content("{\"login\":\"Karma123\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Player not found"));
    }
}
