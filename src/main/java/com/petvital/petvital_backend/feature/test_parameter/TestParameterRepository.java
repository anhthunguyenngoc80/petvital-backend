package com.petvital.petvital_backend.feature.test_parameter;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestParameterRepository
        extends JpaRepository<TestParameter, Integer> {

    List<TestParameter> findAllByIdIn(List<Integer> ids);
}