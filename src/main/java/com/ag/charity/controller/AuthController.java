package com.ag.charity.controller;

import com.ag.charity.DTO.LoginRequestDTO;
import com.ag.charity.DTO.UserResponseDTO;
import com.ag.charity.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        UserResponseDTO response = authService.authenticate(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Déconnexion réussie");
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponseDTO user = authService.getCurrentUser(email);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }
}
