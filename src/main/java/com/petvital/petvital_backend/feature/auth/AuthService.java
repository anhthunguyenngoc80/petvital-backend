package com.petvital.petvital_backend.feature.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.petvital.petvital_backend.config.JwtService;
import com.petvital.petvital_backend.feature.auth.dto.LoginRequest;
import com.petvital.petvital_backend.feature.auth.dto.LoginResponseDto;
import com.petvital.petvital_backend.feature.auth.dto.RegisterRequest;
import com.petvital.petvital_backend.feature.refresh_token.RefreshToken;
import com.petvital.petvital_backend.feature.refresh_token.RefreshTokenRepository;
import com.petvital.petvital_backend.feature.user.User;
import com.petvital.petvital_backend.feature.user.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        String hashPassword = passwordEncoder.encode(request.getPassword());
        user.setPasswordHash(hashPassword);

        userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if(user == null) {
            throw new IllegalArgumentException("Email not existed");
        } 

        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if(!matches) {
            throw new IllegalArgumentException("Incorrect password");
        }

        String accessToken =
                jwtService.generateAccessToken(user);

        String refreshToken =
                jwtService.generateRefreshToken(user);

        RefreshToken refreshTokenEntity = new RefreshToken();

        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setExpiresAt(
                Instant.now().plus(30, ChronoUnit.DAYS)
        );

        refreshTokenRepository.save(refreshTokenEntity);

        return new LoginResponseDto(
                user.getUserId(),
                accessToken,
                refreshToken
        );
    }
}
