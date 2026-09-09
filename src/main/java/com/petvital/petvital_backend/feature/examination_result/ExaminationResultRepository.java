package com.petvital.petvital_backend.feature.examination_result;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExaminationResultRepository
        extends JpaRepository<ExaminationResult, Integer> {

    List<ExaminationResult> findAllByExaminationId(Integer examinationId);
}