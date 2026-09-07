package com.petvital.petvital_backend.feature.reference_range;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReferenceRangeRepository
        extends JpaRepository<ReferenceRange, Integer> {

    List<ReferenceRange> findAllByReferenceRangeIdIn(List<Integer> ids);
}