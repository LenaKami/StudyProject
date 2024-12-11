package com.example.Player;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PlayerApplicationTests {

	@Test
	void contextLoads() {
	}

}

/*    @Test
    void shouldAddPlayerSuccessfully() throws Exception {
        PlayerYT playerYT = new PlayerYT();
        playerYT.setLinkyt("https://youtube.com/somechannel");

        playerYTService.getPlayerByLink(playerYT.getLinkyt()).ifPresent(p -> playerYTService.deletePlayer(p.getId()));  //usuwanie jesli istnieje

        mockMvc.perform(post("/player/add")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + authToken)
                        .content("{\"linkyt\":\"https://youtube.com/somechannel\",\"category\":\"Test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Player added successfully"));
    }

    @Test
    void shouldNotAddPlayerAlreadyExists() throws Exception {
        // Tworzymy gracza z tym samym linkiem YouTube, żeby sprawdzić, czy istnieje
        PlayerYT player = new PlayerYT();
        player.setLinkyt("https://youtube.com/somechannel11");
        player.setCategory("Test");
        playerYTService.addPlayer(player);

        mockMvc.perform(post("/player/add")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + authToken)
                        .content("{\"linkyt\":\"https://youtube.com/somechannel11\",\"category\":\"Test\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Player already exists"));
    }*/