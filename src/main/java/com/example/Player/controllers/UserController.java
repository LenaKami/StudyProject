package com.example.Player.controllers;

import com.example.Player.model.User;
import com.example.Player.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private String secretKey = "6+WsW66HiuWGi5TVsN9PIWsKGXTZfOvbzS189HJt1Ok=";

    @Value("${jwt.expiration}")
    private long expirationTime;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User userRequest) {
        try {
            Optional<User> existingUser = userService.getUserByEmail(userRequest.getEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest().body(
                        Map.of("status", "error", "message", "User already exists")
                );
            }

            String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
            userRequest.setPassword(hashedPassword);
            userRequest.setRole(false);
            userService.saveUser(userRequest);

            return ResponseEntity.ok(
                    Map.of("status", "success", "message", "User registered successfully")
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", e.getMessage())
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            Optional<User> userOptional = userService.getUserByLogin(loginRequest.getLogin());
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Map.of("status", "error", "message", "User not found")
                );
            }

            User user = userOptional.get();

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body(
                        Map.of("status", "error", "message", "Invalid password")
                );
            }

            // Generowanie tokenu JWT
            String token = generateJwtToken(user);

            return ResponseEntity.ok(
                    Map.of("status", "success", "message", "Login successful", "token", token)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("status", "error", "message", e.getMessage())
            );
        }
    }

    private String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject(user.getLogin())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(
                    Map.of("status", "success", "users", users)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", e.getMessage())
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            Optional<User> userOptional = userService.getUserById(id);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(404).body(
                        Map.of("status", "error", "message", "User not found")
                );
            }

            userService.deleteUser(id);

            return ResponseEntity.ok(
                    Map.of("status", "success", "message", "User deleted successfully")
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", e.getMessage())
            );
        }
    }
}
