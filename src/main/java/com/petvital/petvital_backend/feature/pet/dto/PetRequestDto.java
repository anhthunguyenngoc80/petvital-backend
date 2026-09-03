package com.petvital.petvital_backend.feature.pet.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

/**
 * Payload for registering a new pet for the currently authenticated user.
 *
 * <p>The owner is intentionally <b>not</b> part of this request body: it is
 * derived from the authenticated user (JWT token) by the controller/service,
 * so a client can never create a pet owned by someone else (IDOR protection).
 */
public record PetRequestDto(
        @NotBlank
        String name,
        @NotBlank
        String species,
        String breed,
        String sex,
        @PastOrPresent
        LocalDate birthDate,
        @Positive
        Integer weight
) {
}