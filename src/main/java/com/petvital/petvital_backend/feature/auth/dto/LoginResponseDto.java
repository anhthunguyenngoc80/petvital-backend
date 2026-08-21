package com.petvital.petvital_backend.feature.auth.dto;

public record LoginResponseDto(
        String accessToken,
        String refreshToken
) {
}