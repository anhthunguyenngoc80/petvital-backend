package com.petvital.petvital_backend.feature.examination;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExaminationRepository extends JpaRepository<Examination, Integer> {

    List<Examination> findAllByPetId(Integer petId);
}