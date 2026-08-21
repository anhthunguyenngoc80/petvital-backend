package com.petvital.petvital_backend.feature.refresh_token;

import java.util.Optional;

import org.springframework.boot.security.autoconfigure.SecurityProperties.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    void deleteByUser(User user);
}