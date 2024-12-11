package com.example.Player;

import com.example.Player.controllers.UserController;
import com.example.Player.model.User;
import com.example.Player.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");

        userService.getUserByEmail(user.getEmail()).ifPresent(u -> userService.deleteUser(u.getId()));  //usuwanie jesli istnieje

        mockMvc.perform(post("/user/register")
                        .contentType("application/json")
                        .content("{\"email\":\"test@example.com\",\"password\":\"password123\",\"login\":\"testuser\",\"dateOfBirth\":\"1990-01-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void registerUserAlreadyExists() throws Exception {
        mockMvc.perform(post("/user/register")
                        .contentType("application/json")
                        .content("{\"email\":\"test@example.com\",\"password\":\"password123\",\"login\":\"testuser\",\"dateOfBirth\":\"1990-01-01\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("User already exists"));
    }

    @Test
    void shouldLoginUserSuccessfully() throws Exception {

        mockMvc.perform(post("/user/login")
                        .contentType("application/json")
                        .content("{\"password\":\"password123\",\"login\":\"testuser\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void shouldLoginUserNoSuccessfully() throws Exception {
        mockMvc.perform(post("/user/login")
                        .contentType("application/json")
                        .content("{\"password\":\"password12\",\"login\":\"testuser\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Invalid password"));
    }
}
