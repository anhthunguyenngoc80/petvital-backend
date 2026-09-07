package com.petvital.petvital_backend.feature.test_parameter.dto;

import java.util.List;

import com.petvital.petvital_backend.feature.reference_range.dto.ReferenceRangeResponseDto;

public record TestParameterResponseDto(
        Integer parameterId,
        String code,
        String name,
        String category,
        String description,
        String defaultUnit,
        List<ReferenceRangeResponseDto> referenceRanges
) {
}