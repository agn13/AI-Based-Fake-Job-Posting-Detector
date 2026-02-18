package com.fakejobdetector.controller;

import com.fakejobdetector.model.Role;
import com.fakejobdetector.model.User;
import com.fakejobdetector.repository.UserRepository;
import com.fakejobdetector.security.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", user.getRole()
        ));
    }
}
