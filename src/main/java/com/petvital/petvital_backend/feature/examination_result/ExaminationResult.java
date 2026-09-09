package com.petvital.petvital_backend.feature.examination_result;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A single indicator (test parameter) value entered by the user for one
 * examination. Each row links an Examination to a ExaminationParameter (validated
 * to belong to the examination type) together with the value the user
 * filled in.
 *
 * <p>Mapped to the existing {@code test_results} table. The value entered by
 * the user is stored in {@code value_numeric} when it can be parsed as a
 * number, and is always stored in {@code value_text} as well.
 */
@Entity
@Table(name = "examination_results")
public class ExaminationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Integer resultId;

    @Column(name = "examination_id", nullable = false)
    private Integer examinationId;

    @Column(name = "parameter_id", nullable = false)
    private Integer parameterId;

    @Column(name = "value_numeric", precision = 15, scale = 4)
    private BigDecimal valueNumeric;

    @Column(name = "value_text", length = 255)
    private String valueText;

    @Column(name = "unit", length = 50)
    private String unit;

    @Column(name = "flag", length = 20)
    private String flag;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    public Integer getResultId() {
        return resultId;
    }

    public Integer getExaminationId() {
        return examinationId;
    }

    public void setExaminationId(Integer examinationId) {
        this.examinationId = examinationId;
    }

    public Integer getParameterId() {
        return parameterId;
    }

    public void setParameterId(Integer parameterId) {
        this.parameterId = parameterId;
    }

    public BigDecimal getValueNumeric() {
        return valueNumeric;
    }

    public void setValueNumeric(BigDecimal valueNumeric) {
        this.valueNumeric = valueNumeric;
    }

    public String getValueText() {
        return valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}