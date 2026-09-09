package com.petvital.petvital_backend.feature.examination_parameter;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExaminationParameterRepository
        extends JpaRepository<ExaminationParameter, Integer> {

    List<ExaminationParameter> findAllByIdIn(List<Integer> ids);
}