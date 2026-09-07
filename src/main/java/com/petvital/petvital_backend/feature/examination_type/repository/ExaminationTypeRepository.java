package com.petvital.petvital_backend.feature.examination_type.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petvital.petvital_backend.feature.examination_type.entity.ExaminationType;

public interface ExaminationTypeRepository
        extends JpaRepository<ExaminationType, Integer> {
}