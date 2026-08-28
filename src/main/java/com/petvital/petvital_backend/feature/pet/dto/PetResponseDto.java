package com.petvital.petvital_backend.feature.pet.dto;

import java.time.LocalDate;

public record PetResponseDto(
        Integer petId,
        Integer ownerId,
        String name,
        String species,
        String breed,
        String sex,
        LocalDate birthDate,
        Integer weight
) {
}