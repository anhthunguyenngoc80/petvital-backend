package com.petvital.petvital_backend.feature.examination_type.dto;

import java.util.List;

import com.petvital.petvital_backend.feature.test_parameter.dto.TestParameterResponseDto;

public record ExaminationTypeResponseDto(
        Integer examinationTypeId,
        String name,
        String description,
        List<TestParameterResponseDto> parameters
) {
}