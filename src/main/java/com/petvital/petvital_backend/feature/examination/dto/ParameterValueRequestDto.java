package com.petvital.petvital_backend.feature.examination.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Payload for a single indicator value entered by the user when registering
 * an examination result. The {@code parameterId} must belong to the
 * examination type being recorded, otherwise the request is rejected.
 *
 * <p>The value is free text; when it can be parsed as a number it is also
 * stored in the numeric column of {@code test_results}.
 */
public record ParameterValueRequestDto(
        @NotNull
        Integer parameterId,

        @NotBlank
        String value,

        String unit,

        String flag,

        String notes
) {
}