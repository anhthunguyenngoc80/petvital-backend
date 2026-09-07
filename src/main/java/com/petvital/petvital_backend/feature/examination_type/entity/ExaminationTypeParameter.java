package com.petvital.petvital_backend.feature.examination_type.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "examination_type_parameters")
public class ExaminationTypeParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "examination_type_id", nullable = false)
    private Integer examinationTypeId;

    @Column(name = "parameter_id", nullable = false)
    private Integer parameterId;

    @Column(name = "reference_range_id", nullable = false)
    private Integer referenceRangeId;

    public Integer getId() {
        return id;
    }

    public Integer getExaminationTypeId() {
        return examinationTypeId;
    }

    public void setExaminationTypeId(Integer examinationTypeId) {
        this.examinationTypeId = examinationTypeId;
    }

    public Integer getParameterId() {
        return parameterId;
    }

    public void setParameterId(Integer parameterId) {
        this.parameterId = parameterId;
    }

    public Integer getReferenceRangeId() {
        return referenceRangeId;
    }

    public void setReferenceRangeId(Integer referenceRangeId) {
        this.referenceRangeId = referenceRangeId;
    }
}