package com.petvital.petvital_backend.feature.examination.dto;

import java.time.LocalDateTime;

/**
 * Payload describing a single laboratory/test examination performed on a pet.
 */
public record ExaminationResponseDto(
        Integer examinationId,
        Integer petId,
        Integer examinationTypeId,
        LocalDateTime examinationDate,
        String clinicName,
        String doctorName,
        String diagnosis,
        String notes
) {
}