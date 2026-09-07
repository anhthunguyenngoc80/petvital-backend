package com.petvital.petvital_backend.feature.examination;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.petvital.petvital_backend.feature.examination.dto.ExaminationRequestDto;
import com.petvital.petvital_backend.feature.examination.dto.ExaminationResponseDto;
import com.petvital.petvital_backend.feature.examination.entity.Examination;
import com.petvital.petvital_backend.feature.pet.Pet;
import com.petvital.petvital_backend.feature.pet.PetRepository;
import com.petvital.petvital_backend.feature.user.User;

@Service
public class ExaminationService {

    private final ExaminationRepository examinationRepository;
    private final PetRepository petRepository;

    public ExaminationService(ExaminationRepository examinationRepository,
            PetRepository petRepository) {
        this.examinationRepository = examinationRepository;
        this.petRepository = petRepository;
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

    /**
     * Registers a new test/lab result (examination) for the pet identified by
     * {@code petId}.
     *
     * <p>The pet must exist and must be owned by {@code owner} (the currently
     * authenticated user); otherwise a NOT_FOUND / FORBIDDEN response is raised
     * so a client can never attach a result to a pet belonging to someone else
     * (IDOR protection).
     */
    @Transactional
    public ExaminationResponseDto registerExamination(Integer petId,
            ExaminationRequestDto request, User owner) {

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pet not found with id: " + petId));

        if (!pet.getOwnerId().equals(owner.getUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to register results for this pet");
        }

        Examination examination = new Examination();
        examination.setPetId(petId);
        examination.setExaminationTypeId(request.examinationTypeId());
        examination.setExaminationDate(request.examinationDate());
        examination.setClinicName(request.clinicName());
        examination.setDoctorName(request.doctorName());
        examination.setDiagnosis(request.diagnosis());
        examination.setNotes(request.notes());

        Examination saved = examinationRepository.save(examination);

        return toResponseDto(saved);
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