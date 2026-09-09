package com.petvital.petvital_backend.feature.examination.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.petvital.petvital_backend.feature.examination_type.dto.ExaminationTypeSummaryDto;

/**
 * Full payload describing one recorded examination of a pet, including the
 * examination type and every indicator value recorded for it.
 */
public record ExaminationResponseDto(
        Integer examinationId,
        Integer petId,
        ExaminationTypeSummaryDto examinationType,
        LocalDateTime examinationDate,
        String clinicName,
        String doctorName,
        String diagnosis,
        String notes,
        List<ExaminationParameterResultDto> values
) {
}