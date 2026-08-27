package com.petvital.petvital_backend.feature.auth.dto;

public record LoginResponseDto(
        Integer userId,
        String accessToken,
        String refreshToken
) {
}