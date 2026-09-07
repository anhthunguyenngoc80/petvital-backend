package com.petvital.petvital_backend.feature.examination_type;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petvital.petvital_backend.feature.examination_type.dto.CreateExaminationTypeRequestDto;
import com.petvital.petvital_backend.feature.examination_type.dto.ExaminationTypeResponseDto;
import com.petvital.petvital_backend.feature.examination_type.dto.ExaminationTypeSummaryDto;
import com.petvital.petvital_backend.feature.examination_type.entity.ExaminationType;
import com.petvital.petvital_backend.feature.examination_type.entity.ExaminationTypeParameter;
import com.petvital.petvital_backend.feature.examination_type.repository.ExaminationTypeParameterRepository;
import com.petvital.petvital_backend.feature.examination_type.repository.ExaminationTypeRepository;
import com.petvital.petvital_backend.feature.reference_range.ReferenceRange;
import com.petvital.petvital_backend.feature.reference_range.ReferenceRangeRepository;
import com.petvital.petvital_backend.feature.reference_range.dto.ReferenceRangeRequestDto;
import com.petvital.petvital_backend.feature.reference_range.dto.ReferenceRangeResponseDto;
import com.petvital.petvital_backend.feature.test_parameter.TestParameter;
import com.petvital.petvital_backend.feature.test_parameter.TestParameterRepository;
import com.petvital.petvital_backend.feature.test_parameter.dto.TestParameterRequestDto;
import com.petvital.petvital_backend.feature.test_parameter.dto.TestParameterResponseDto;

@Service
public class ExaminationTypeService {

    private final ExaminationTypeRepository examinationTypeRepository;
    private final TestParameterRepository testParameterRepository;
    private final ReferenceRangeRepository referenceRangeRepository;
    private final ExaminationTypeParameterRepository
            examinationTypeParameterRepository;

    public ExaminationTypeService(
            ExaminationTypeRepository examinationTypeRepository,
            TestParameterRepository testParameterRepository,
            ReferenceRangeRepository referenceRangeRepository,
            ExaminationTypeParameterRepository examinationTypeParameterRepository) {
        this.examinationTypeRepository = examinationTypeRepository;
        this.testParameterRepository = testParameterRepository;
        this.referenceRangeRepository = referenceRangeRepository;
        this.examinationTypeParameterRepository =
                examinationTypeParameterRepository;
    }

    @Transactional
    public ExaminationTypeResponseDto createExaminationType(
            CreateExaminationTypeRequestDto request) {

        ExaminationType type = new ExaminationType();
        type.setName(request.name());
        type.setDescription(request.description());
        ExaminationType savedType = examinationTypeRepository.save(type);

        List<TestParameterResponseDto> parameterResponses = new ArrayList<>();

        for (TestParameterRequestDto parameterRequest : request.parameters()) {

            TestParameter parameter = new TestParameter();
            parameter.setCode(parameterRequest.code());
            parameter.setName(parameterRequest.name());
            parameter.setCategory(parameterRequest.category());
            parameter.setDescription(parameterRequest.description());
            parameter.setDefaultUnit(parameterRequest.defaultUnit());
            TestParameter savedParameter = testParameterRepository.save(parameter);

            List<ReferenceRangeResponseDto> rangeResponses = new ArrayList<>();

            List<ReferenceRangeRequestDto> ranges = parameterRequest.referenceRanges();
            if (ranges != null) {
                for (ReferenceRangeRequestDto rangeRequest : ranges) {

                    ReferenceRange range = new ReferenceRange();
                    range.setSpecies(rangeRequest.species());
                    range.setSex(rangeRequest.sex());
                    range.setAgeMin(rangeRequest.ageMin());
                    range.setAgeMax(rangeRequest.ageMax());
                    range.setMinValue(rangeRequest.minValue());
                    range.setMaxValue(rangeRequest.maxValue());
                    range.setUnit(rangeRequest.unit());
                    range.setLabName(rangeRequest.labName());
                    ReferenceRange savedRange = referenceRangeRepository.save(range);

                    ExaminationTypeParameter link = new ExaminationTypeParameter();
                    link.setExaminationTypeId(savedType.getExaminationTypeId());
                    link.setParameterId(savedParameter.getId());
                    link.setReferenceRangeId(savedRange.getReferenceRangeId());
                    examinationTypeParameterRepository.save(link);

                    rangeResponses.add(toRangeResponse(savedRange));
                }
            }

            parameterResponses.add(new TestParameterResponseDto(
                    savedParameter.getId(),
                    savedParameter.getCode(),
                    savedParameter.getName(),
                    savedParameter.getCategory(),
                    savedParameter.getDescription(),
                    savedParameter.getDefaultUnit(),
                    rangeResponses));
        }

        return new ExaminationTypeResponseDto(
                savedType.getExaminationTypeId(),
                savedType.getName(),
                savedType.getDescription(),
                parameterResponses);
    }

    /**
     * Returns the list of all examination types.
     */
    @Transactional(readOnly = true)
    public List<ExaminationTypeSummaryDto> getAllExaminationTypes() {
        return examinationTypeRepository.findAll().stream()
                .map(type -> new ExaminationTypeSummaryDto(
                        type.getExaminationTypeId(),
                        type.getName(),
                        type.getDescription()))
                .toList();
    }

    private ReferenceRangeResponseDto toRangeResponse(ReferenceRange range) {
        return new ReferenceRangeResponseDto(
                range.getReferenceRangeId(),
                range.getSpecies(),
                range.getSex(),
                range.getAgeMin(),
                range.getAgeMax(),
                range.getMinValue(),
                range.getMaxValue(),
                range.getUnit(),
                range.getLabName());
    }
}