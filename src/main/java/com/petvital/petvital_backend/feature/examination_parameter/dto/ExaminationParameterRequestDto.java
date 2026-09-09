package com.petvital.petvital_backend.feature.examination_parameter.dto;

import java.util.List;

import com.petvital.petvital_backend.feature.reference_range.dto.ReferenceRangeRequestDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * Payload for a single test parameter (indicator) attached to an examination
 * type, together with its reference ranges.
 */
public record ExaminationParameterRequestDto(
        @NotBlank
        String code,
        @NotBlank
        String name,
        String category,
        String description,
        String defaultUnit,
        @Valid
        List<ReferenceRangeRequestDto> referenceRanges
) {
}