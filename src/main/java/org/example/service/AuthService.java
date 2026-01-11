package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ============================
    // REGISTER
    // ============================
    public User register(User user) {
        user.setRole("USER");
        return userRepository.save(user);
    }

    // ============================
    // LOGIN (AUTHENTICATION)
    // ============================
    public User authenticate(String username, String password) {

        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password)) // (plain-text for now)
                .orElse(null);
    }
}
