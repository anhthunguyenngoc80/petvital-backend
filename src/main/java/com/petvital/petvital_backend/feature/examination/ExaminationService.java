package com.petvital.petvital_backend.feature.examination;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petvital.petvital_backend.feature.examination.dto.ExaminationResponseDto;

@Service
public class ExaminationService {

    private final ExaminationRepository examinationRepository;

    public ExaminationService(ExaminationRepository examinationRepository) {
        this.examinationRepository = examinationRepository;
    }

    /**
     * Returns the list of test/lab results (examinations) recorded for the pet
     * identified by {@code petId}, ordered by the examination date descending
     * so the most recent result comes first.
     */
    @Transactional(readOnly = true)
    public List<ExaminationResponseDto> getExaminationsByPet(Integer petId) {
        return examinationRepository.findAllByPetId(petId).stream()
                .sorted((a, b) -> b.getExaminationDate()
                        .compareTo(a.getExaminationDate()))
                .map(this::toResponseDto)
                .toList();
    }

    private ExaminationResponseDto toResponseDto(Examination examination) {
        return new ExaminationResponseDto(
                examination.getExaminationId(),
                examination.getPetId(),
                examination.getExaminationTypeId(),
                examination.getExaminationDate(),
                examination.getClinicName(),
                examination.getDoctorName(),
                examination.getDiagnosis(),
                examination.getNotes()
        );
    }
}