package com.petvital.petvital_backend.feature.pet.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record PetRequestDto(
        @NotBlank
        Integer ownerId,
        @NotBlank
        String name,
        @NotBlank
        String species,
        String breed,
        String sex,
        LocalDate birthDate,
        Integer weight
) {
}