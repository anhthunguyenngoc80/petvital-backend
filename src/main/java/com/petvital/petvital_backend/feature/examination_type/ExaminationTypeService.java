package com.petvital.petvital_backend.feature.examination_type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petvital.petvital_backend.feature.examination_parameter.ExaminationParameter;
import com.petvital.petvital_backend.feature.examination_parameter.ExaminationParameterRepository;
import com.petvital.petvital_backend.feature.examination_parameter.dto.ExaminationParameterRequestDto;
import com.petvital.petvital_backend.feature.examination_parameter.dto.ExaminationParameterResponseDto;
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

@Service
public class ExaminationTypeService {

    private final ExaminationTypeRepository examinationTypeRepository;
    private final ExaminationParameterRepository ExaminationParameterRepository;
    private final ReferenceRangeRepository referenceRangeRepository;
    private final ExaminationTypeParameterRepository
            examinationTypeParameterRepository;

    public ExaminationTypeService(
            ExaminationTypeRepository examinationTypeRepository,
            ExaminationParameterRepository ExaminationParameterRepository,
            ReferenceRangeRepository referenceRangeRepository,
            ExaminationTypeParameterRepository examinationTypeParameterRepository) {
        this.examinationTypeRepository = examinationTypeRepository;
        this.ExaminationParameterRepository = ExaminationParameterRepository;
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

        List<ExaminationParameterResponseDto> parameterResponses = new ArrayList<>();

        for (ExaminationParameterRequestDto parameterRequest : request.parameters()) {

            ExaminationParameter parameter = new ExaminationParameter();
            parameter.setCode(parameterRequest.code());
            parameter.setName(parameterRequest.name());
            parameter.setCategory(parameterRequest.category());
            parameter.setDescription(parameterRequest.description());
            parameter.setDefaultUnit(parameterRequest.defaultUnit());
            ExaminationParameter savedParameter = ExaminationParameterRepository.save(parameter);

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

            parameterResponses.add(new ExaminationParameterResponseDto(
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

    /**
     * Returns one examination type together with all its test parameters
     * (indicators) and each parameter's reference ranges.
     */
    @Transactional(readOnly = true)
    public ExaminationTypeResponseDto getExaminationTypeById(
            Integer examinationTypeId) {

        ExaminationType type = examinationTypeRepository
                .findById(examinationTypeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Examination type not found: " + examinationTypeId));

        List<ExaminationTypeParameter> links =
                examinationTypeParameterRepository
                        .findAllByExaminationTypeId(examinationTypeId);

        List<ExaminationParameterResponseDto> parameterResponses = new ArrayList<>();

        if (!links.isEmpty()) {
            List<Integer> parameterIds = links.stream()
                    .map(ExaminationTypeParameter::getParameterId)
                    .distinct()
                    .toList();

            List<Integer> rangeIds = links.stream()
                    .map(ExaminationTypeParameter::getReferenceRangeId)
                    .toList();

            Map<Integer, ExaminationParameter> parametersById =
                    ExaminationParameterRepository.findAllByIdIn(parameterIds)
                            .stream()
                            .collect(Collectors.toMap(
                                    ExaminationParameter::getId, p -> p));

            Map<Integer, List<ReferenceRange>> rangesByRangeId =
                    new HashMap<>();
            for (ReferenceRange range : referenceRangeRepository
                    .findAllByReferenceRangeIdIn(rangeIds)) {
                rangesByRangeId
                        .computeIfAbsent(range.getReferenceRangeId(),
                                k -> new ArrayList<>())
                        .add(range);
            }

            for (ExaminationTypeParameter link : links) {
                ExaminationParameter parameter =
                        parametersById.get(link.getParameterId());
                if (parameter == null) {
                    continue;
                }

                List<ReferenceRangeResponseDto> rangeResponses =
                        rangesByRangeId
                                .getOrDefault(link.getReferenceRangeId(),
                                        List.of())
                                .stream()
                                .map(this::toRangeResponse)
                                .toList();

                parameterResponses.add(new ExaminationParameterResponseDto(
                        parameter.getId(),
                        parameter.getCode(),
                        parameter.getName(),
                        parameter.getCategory(),
                        parameter.getDescription(),
                        parameter.getDefaultUnit(),
                        rangeResponses));
            }
        }

        return new ExaminationTypeResponseDto(
                type.getExaminationTypeId(),
                type.getName(),
                type.getDescription(),
                parameterResponses);
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