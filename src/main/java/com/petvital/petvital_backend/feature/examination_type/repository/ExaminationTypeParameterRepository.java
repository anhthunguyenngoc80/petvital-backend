package com.petvital.petvital_backend.feature.examination_type.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petvital.petvital_backend.feature.examination_type.entity.ExaminationTypeParameter;

public interface ExaminationTypeParameterRepository
        extends JpaRepository<ExaminationTypeParameter, Integer> {

    List<ExaminationTypeParameter> findAllByExaminationTypeId(
            Integer examinationTypeId);
}