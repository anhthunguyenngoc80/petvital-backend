package com.petvital.petvital_backend.feature.reference_range.dto;

import java.math.BigDecimal;

public record ReferenceRangeResponseDto(
        Integer referenceRangeId,
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