package com.petvital.petvital_backend.feature.examination;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petvital.petvital_backend.feature.examination.dto.ExaminationResponseDto;

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
}