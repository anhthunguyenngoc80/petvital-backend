package com.petvital.petvital_backend.feature.examination_type.dto;

public record ExaminationTypeSummaryDto(
        Integer examinationTypeId,
        String name,
        String description
) {
}