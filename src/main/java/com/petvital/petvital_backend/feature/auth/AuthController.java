package com.petvital.petvital_backend.feature.auth;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petvital.petvital_backend.feature.auth.dto.LoginRequest;
import com.petvital.petvital_backend.feature.auth.dto.LoginResponseDto;
import com.petvital.petvital_backend.feature.auth.dto.RegisterRequest;
import com.petvital.petvital_backend.feature.auth.dto.RefreshTokenRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        authService.registerUser(request);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequest request) {
        LoginResponseDto response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();
        LoginResponseDto response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
}
