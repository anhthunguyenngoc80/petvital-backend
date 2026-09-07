package com.petvital.petvital_backend.feature.examination;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petvital.petvital_backend.feature.examination.entity.Examination;

public interface ExaminationRepository extends JpaRepository<Examination, Integer> {

    List<Examination> findAllByPetId(Integer petId);
}