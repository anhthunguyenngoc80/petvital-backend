package com.petvital.petvital_backend.feature.examination.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

/**
 * Payload for registering a new test/lab result (examination) for a pet.
 *
 * <p>The pet id is intentionally <b>not</b> part of this request body: it is
 * taken from the URL path and validated against the currently authenticated
 * user, so a client can never attach a result to a pet owned by someone else
 * (IDOR protection).
 */
public record ExaminationRequestDto(
        @NotNull
        Integer examinationTypeId,
        @NotNull
        LocalDateTime examinationDate,
        String clinicName,
        String doctorName,
        String diagnosis,
        String notes
) {
}