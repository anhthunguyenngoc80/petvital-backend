package com.petvital.petvital_backend.feature.examination;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petvital.petvital_backend.feature.examination.dto.ExaminationRequestDto;
import com.petvital.petvital_backend.feature.examination.dto.ExaminationResponseDto;
import com.petvital.petvital_backend.feature.user.User;

@RestController
@RequestMapping("/api")
public class ExaminationController {

    private final ExaminationService examinationService;

    public ExaminationController(ExaminationService examinationService) {
        this.examinationService = examinationService;
    }

    /**
     * Returns the list of test/lab results (examinations) for the pet
     * identified by its id.
     *
     * Example: GET /api/pets/3/examinations
     */
    @GetMapping("/pets/{petId}/examinations")
    public ResponseEntity<List<ExaminationResponseDto>> getExaminationsByPet(
            @PathVariable Integer petId) {

        List<ExaminationResponseDto> examinations =
                examinationService.getExaminationsByPet(petId);

        return ResponseEntity.ok(examinations);
    }

    /**
     * Registers a new test/lab result (examination) for the pet identified by
     * its id. The authenticated user must be the owner of that pet.
     *
     * Example: POST /api/pets/3/examinations
     */
    @PostMapping("/pets/{petId}/examinations")
    public ResponseEntity<ExaminationResponseDto> registerExamination(
            @PathVariable Integer petId,
            @Valid @RequestBody ExaminationRequestDto request,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        ExaminationResponseDto response =
                examinationService.registerExamination(petId, request, currentUser);

        return ResponseEntity.ok(response);
    }
}