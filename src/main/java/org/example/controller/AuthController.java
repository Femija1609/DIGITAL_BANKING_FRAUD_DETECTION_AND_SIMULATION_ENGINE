package org.example.controller;

import org.example.model.User;
import org.example.security.JwtUtil;
import org.example.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    // ============================
    // REGISTER
    // ============================
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return authService.register(user);
    }

    // ============================
    // LOGIN
    // ============================
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {

        User authenticatedUser =
                authService.authenticate(user.getUsername(), user.getPassword());

        if (authenticatedUser == null) {
            return Map.of(
                    "success", false,
                    "message", "Invalid username or password"
            );
        }

        String token = jwtUtil.generateToken(authenticatedUser.getUsername());

        return Map.of(
                "success", true,
                "token", token,
                "username", authenticatedUser.getUsername(),
                "role", authenticatedUser.getRole()
        );
    }
}
