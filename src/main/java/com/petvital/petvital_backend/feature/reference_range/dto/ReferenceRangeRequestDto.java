package com.petvital.petvital_backend.feature.reference_range.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

/**
 * Payload for a single reference range associated with a test parameter.
 */
public record ReferenceRangeRequestDto(
        @NotBlank
        String species,
        String sex,
        BigDecimal ageMin,
        BigDecimal ageMax,
        BigDecimal minValue,
        BigDecimal maxValue,
        String unit,
        String labName
) {
}