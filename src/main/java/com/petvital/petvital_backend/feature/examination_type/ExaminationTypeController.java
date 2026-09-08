package com.petvital.petvital_backend.feature.examination_type;

import jakarta.validation.Valid;

import com.petvital.petvital_backend.feature.examination_type.dto.ExaminationTypeSummaryDto;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petvital.petvital_backend.feature.examination_type.dto.CreateExaminationTypeRequestDto;
import com.petvital.petvital_backend.feature.examination_type.dto.ExaminationTypeResponseDto;

@RestController
@RequestMapping("/api/examination-types")
public class ExaminationTypeController {

    private final ExaminationTypeService examinationTypeService;

    public ExaminationTypeController(
            ExaminationTypeService examinationTypeService) {
        this.examinationTypeService = examinationTypeService;
    }

    /**
     * Returns the list of all examination types.
     *
     * Example: GET /api/examination-types
     */
    @GetMapping
    public ResponseEntity<List<ExaminationTypeSummaryDto>> getAllExaminationTypes() {
        List<ExaminationTypeSummaryDto> response =
                examinationTypeService.getAllExaminationTypes();
        return ResponseEntity.ok(response);
    }

    /**
     * Returns one examination type together with all its test parameters
     * (indicators) and each parameter's reference values.
     *
     * Example: GET /api/examination-types/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExaminationTypeResponseDto> getExaminationTypeById(
            @PathVariable("id") Integer id) {

        ExaminationTypeResponseDto response =
                examinationTypeService.getExaminationTypeById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new examination type together with its test parameters
     * (indicators) and their reference ranges.
     *
     * Example: POST /api/examination-types
     */
    @PostMapping
        public ResponseEntity<ExaminationTypeResponseDto> createExaminationType(
            @Valid @RequestBody CreateExaminationTypeRequestDto request) {

        ExaminationTypeResponseDto response =
            examinationTypeService.createExaminationType(request);

        return ResponseEntity.ok(response);
        }
}