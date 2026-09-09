package com.petvital.petvital_backend.feature.examination.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Payload for registering a new test/lab result (examination) for a pet.
 *
 * <p>The pet id is intentionally <b>not</b> part of this request body: it is
 * taken from the URL path and validated against the currently authenticated
 * user, so a client can never attach a result to a pet owned by someone else
 * (IDOR protection).
 *
 * <p>Example:
 * <pre>{@code
 * {
 *   "examinationTypeId": 1,
 *   "examinationDate": "2026-09-09T10:30:00",
 *   "clinicName": "PetVital Clinic",
 *   "doctorName": "BS. Nguyen Van A",
 *   "diagnosis": "Thiếu máu nhẹ",
 *   "notes": "Tái khám sau 2 tuần",
 *   "values": [
 *     { "parameterId": 5, "value": "12.3", "unit": "10^9/L", "flag": "normal" },
 *     { "parameterId": 6, "value": "35.5", "unit": "g/L" }
 *   ]
 * }
 * }</pre>
 * Each {@code values[].parameterId} must belong to the examination type,
 * otherwise the request is rejected with 400 BAD_REQUEST.
 */
public record ExaminationRequestDto(
        @NotNull
        Integer examinationTypeId,

        @NotNull
        LocalDateTime examinationDate,

        String clinicName,

        String doctorName,

        String diagnosis,

        String notes,

        @NotNull
        @NotEmpty
        @Valid
        List<ParameterValueRequestDto> values
) {
}