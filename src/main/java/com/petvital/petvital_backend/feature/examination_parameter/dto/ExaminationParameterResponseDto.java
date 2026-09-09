package com.petvital.petvital_backend.feature.examination_parameter.dto;

import java.util.List;

import com.petvital.petvital_backend.feature.reference_range.dto.ReferenceRangeResponseDto;

public record ExaminationParameterResponseDto(
        Integer parameterId,
        String code,
        String name,
        String category,
        String description,
        String defaultUnit,
        List<ReferenceRangeResponseDto> referenceRanges
) {
}