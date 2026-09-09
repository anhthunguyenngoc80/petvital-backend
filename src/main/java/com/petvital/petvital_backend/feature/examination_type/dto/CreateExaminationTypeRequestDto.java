package com.petvital.petvital_backend.feature.examination_type.dto;

import java.util.List;

import com.petvital.petvital_backend.feature.examination_parameter.dto.ExaminationParameterRequestDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Payload for creating a new examination type together with its test
 * parameters (indicators) and their reference ranges.
 *
 * <p>Example:
 * <pre>{@code
 * {
 *   "name": "Complete Blood Count",
 *   "description": "Standard CBC panel",
 *   "parameters": [
 *     {
 *       "code": "WBC",
 *       "name": "White Blood Cells",
 *       "category": "hematology",
 *       "defaultUnit": "10^9/L",
 *       "referenceRanges": [
 *         { "species": "dog", "minValue": 5.5, "maxValue": 17.0, "unit": "10^9/L" }
 *       ]
 *     }
 *   ]
 * }
 * }</pre>
 */
public record CreateExaminationTypeRequestDto(
        @NotBlank
        String name,
        String description,
        @NotNull
        @Valid
        List<ExaminationParameterRequestDto> parameters
) {
}