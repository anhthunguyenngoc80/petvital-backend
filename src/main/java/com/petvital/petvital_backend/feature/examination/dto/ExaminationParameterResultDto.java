package com.petvital.petvital_backend.feature.examination.dto;

import java.math.BigDecimal;

import com.petvital.petvital_backend.feature.reference_range.dto.ReferenceRangeResponseDto;

/**
 * A single indicator result of an examination: the value the user entered,
 * enriched with the parameter information and the reference range of the
 * examination type so the client can compare value vs. expected range.
 */
public record ExaminationParameterResultDto(
        Integer resultId,
        Integer parameterId,
        String parameterCode,
        String parameterName,
        String category,
        BigDecimal valueNumeric,
        String valueText,
        String unit,
        String flag,
        String notes,
        ReferenceRangeResponseDto referenceRange
) {
}