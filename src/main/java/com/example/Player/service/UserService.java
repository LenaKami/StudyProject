package com.example.Player.service;

import com.example.Player.model.User;
import com.example.Player.repository.UserRepository;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    //private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Optional<User> registerUser(String login, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        User newUser = User.builder()
                .login(login)
                .email(email)
                .password(password)
                //.password(passwordEncoder.encode(password))
                .role(false)
                .build();

        return Optional.of(userRepository.save(newUser));
    }/*

    /*public boolean verifyPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }*/

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

//    public User saveUser(User user) {
//        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
//            throw new IllegalArgumentException("User already exists");
//        }
//        return userRepository.save(user);
//    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    }
