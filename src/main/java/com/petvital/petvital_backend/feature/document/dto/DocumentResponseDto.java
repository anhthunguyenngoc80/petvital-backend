package com.petvital.petvital_backend.feature.document.dto;

import java.time.LocalDateTime;

public record DocumentResponseDto(
        Integer documentId,
        Integer examinationId,
        String originalFilename,
        String contentType,
        LocalDateTime uploadedAt) {
}
