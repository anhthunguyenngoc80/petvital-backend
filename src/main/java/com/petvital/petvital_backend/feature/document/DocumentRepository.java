package com.petvital.petvital_backend.feature.document;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petvital.petvital_backend.feature.document.entity.Document;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    List<Document> findAllByExaminationId(Integer examinationId);
}
