package com.petvital.petvital_backend.feature.examination;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.petvital.petvital_backend.feature.examination.dto.ExaminationParameterResultDto;
import com.petvital.petvital_backend.feature.examination.dto.ExaminationRequestDto;
import com.petvital.petvital_backend.feature.examination.dto.ExaminationResponseDto;
import com.petvital.petvital_backend.feature.examination.dto.ParameterValueRequestDto;
import com.petvital.petvital_backend.feature.examination.entity.Examination;
import com.petvital.petvital_backend.feature.examination_parameter.ExaminationParameter;
import com.petvital.petvital_backend.feature.examination_parameter.ExaminationParameterRepository;
import com.petvital.petvital_backend.feature.examination_result.ExaminationResult;
import com.petvital.petvital_backend.feature.examination_result.ExaminationResultRepository;
import com.petvital.petvital_backend.feature.examination_type.repository.ExaminationTypeRepository;
import com.petvital.petvital_backend.feature.examination_type.dto.ExaminationTypeSummaryDto;
import com.petvital.petvital_backend.feature.examination_type.entity.ExaminationType;
import com.petvital.petvital_backend.feature.examination_type.entity.ExaminationTypeParameter;
import com.petvital.petvital_backend.feature.examination_type.repository.ExaminationTypeParameterRepository;
import com.petvital.petvital_backend.feature.pet.Pet;
import com.petvital.petvital_backend.feature.pet.PetRepository;
import com.petvital.petvital_backend.feature.reference_range.ReferenceRange;
import com.petvital.petvital_backend.feature.reference_range.ReferenceRangeRepository;
import com.petvital.petvital_backend.feature.reference_range.dto.ReferenceRangeResponseDto;
import com.petvital.petvital_backend.feature.user.User;

@Service
public class ExaminationService {

    private final ExaminationRepository examinationRepository;
    private final ExaminationResultRepository examinationResultRepository;
    private final PetRepository petRepository;
    private final ExaminationTypeRepository examinationTypeRepository;
    private final ExaminationTypeParameterRepository examinationTypeParameterRepository;
    private final ExaminationParameterRepository ExaminationParameterRepository;
    private final ReferenceRangeRepository referenceRangeRepository;

    public ExaminationService(ExaminationRepository examinationRepository,
            ExaminationResultRepository examinationResultRepository,
            PetRepository petRepository,
            ExaminationTypeRepository examinationTypeRepository,
            ExaminationTypeParameterRepository examinationTypeParameterRepository,
            ExaminationParameterRepository ExaminationParameterRepository,
            ReferenceRangeRepository referenceRangeRepository) {
        this.examinationRepository = examinationRepository;
        this.examinationResultRepository = examinationResultRepository;
        this.petRepository = petRepository;
        this.examinationTypeRepository = examinationTypeRepository;
        this.examinationTypeParameterRepository = examinationTypeParameterRepository;
        this.ExaminationParameterRepository = ExaminationParameterRepository;
        this.referenceRangeRepository = referenceRangeRepository;
    }

    /**
     * Returns the list of test/lab results (examinations) recorded for the pet
     * identified by {@code petId}, ordered by the examination date descending
     * so the most recent result comes first. Each examination includes its
     * type and the values of all recorded indicators.
     */
    @Transactional(readOnly = true)
    public List<ExaminationResponseDto> getExaminationsByPet(Integer petId) {
        return examinationRepository.findAllByPetId(petId).stream()
                .sorted((a, b) -> b.getExaminationDate()
                        .compareTo(a.getExaminationDate()))
                .map(this::toResponseDto)
                .toList();
    }

    /**
     * Registers a new test/lab result (examination) for the pet identified by
     * {@code petId}, together with the values of the indicators the user
     * filled in for that examination type.
     *
     * <p>Validations: the pet must exist and be owned by {@code owner}
     * (IDOR protection); the examination type must exist; every
     * {@code parameterId} must belong to that examination type and appear
     * only once.
     */
    @Transactional
    public ExaminationResponseDto registerExamination(Integer petId,
            ExaminationRequestDto request, User owner) {

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pet not found with id: " + petId));

        if (!pet.getOwnerId().equals(owner.getUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to register results for this pet");
        }

        ExaminationType type = examinationTypeRepository
                .findById(request.examinationTypeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Examination type not found with id: "
                                + request.examinationTypeId()));

        Map<Integer, Integer> parameterToRangeId =
                loadParameterToRangeId(type.getExaminationTypeId());

        Set<Integer> seenParameterIds = new HashSet<>();
        for (ParameterValueRequestDto value : request.values()) {
            Integer parameterId = value.parameterId();
            if (!parameterToRangeId.containsKey(parameterId)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Parameter " + parameterId
                                + " does not belong to examination type '"
                                + type.getName() + "'");
            }
            if (!seenParameterIds.add(parameterId)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Duplicate value for parameter " + parameterId);
            }
        }

        Examination examination = new Examination();
        examination.setPetId(petId);
        examination.setExaminationTypeId(type.getExaminationTypeId());
        examination.setExaminationDate(request.examinationDate());
        examination.setClinicName(request.clinicName());
        examination.setDoctorName(request.doctorName());
        examination.setDiagnosis(request.diagnosis());
        examination.setNotes(request.notes());

        Examination saved = examinationRepository.save(examination);

        for (ParameterValueRequestDto value : request.values()) {
            ExaminationResult result = new ExaminationResult();
            result.setExaminationId(saved.getExaminationId());
            result.setParameterId(value.parameterId());
            result.setValueText(value.value());
            result.setValueNumeric(parseNumeric(value.value()));
            result.setUnit(value.unit());
            result.setFlag(value.flag());
            result.setNotes(value.notes());
            examinationResultRepository.save(result);
        }

        return toResponseDto(saved);
    }

    private BigDecimal parseNumeric(String raw) {
        try {
            return new BigDecimal(raw.trim());
        } catch (NumberFormatException | NullPointerException ex) {
            return null;
        }
    }

    private Map<Integer, Integer> loadParameterToRangeId(
            Integer examinationTypeId) {

        Map<Integer, Integer> parameterToRangeId = new HashMap<>();
        for (ExaminationTypeParameter link : examinationTypeParameterRepository
                .findAllByExaminationTypeId(examinationTypeId)) {
            parameterToRangeId.put(link.getParameterId(),
                    link.getReferenceRangeId());
        }
        return parameterToRangeId;
    }

    private ExaminationResponseDto toResponseDto(Examination examination) {

        ExaminationType type = examinationTypeRepository
                .findById(examination.getExaminationTypeId())
                .orElse(null);

        ExaminationTypeSummaryDto typeSummary = type == null ? null
                : new ExaminationTypeSummaryDto(
                        type.getExaminationTypeId(),
                        type.getName(),
                        type.getDescription());

        Map<Integer, Integer> parameterToRangeId = type == null
                ? Map.of()
                : loadParameterToRangeId(type.getExaminationTypeId());

        List<ExaminationResult> results = examinationResultRepository
                .findAllByExaminationId(examination.getExaminationId());

        Set<Integer> parameterIds = new HashSet<>();
        Set<Integer> rangeIds = new HashSet<>();
        for (ExaminationResult result : results) {
            parameterIds.add(result.getParameterId());
            Integer rangeId = parameterToRangeId.get(result.getParameterId());
            if (rangeId != null) {
                rangeIds.add(rangeId);
            }
        }

        Map<Integer, ExaminationParameter> parametersById = parameterIds.isEmpty()
                ? Map.of()
                : ExaminationParameterRepository
                        .findAllByIdIn(new ArrayList<>(parameterIds)).stream()
                        .collect(java.util.stream.Collectors.toMap(
                                ExaminationParameter::getId, p -> p));

        Map<Integer, ReferenceRange> rangesById = rangeIds.isEmpty()
                ? Map.of()
                : referenceRangeRepository
                        .findAllByReferenceRangeIdIn(
                                new ArrayList<>(rangeIds))
                        .stream()
                        .collect(java.util.stream.Collectors.toMap(
                                ReferenceRange::getReferenceRangeId,
                                r -> r));

        List<ExaminationParameterResultDto> valueDtos = new ArrayList<>();
        for (ExaminationResult result : results) {
            ExaminationParameter parameter =
                    parametersById.get(result.getParameterId());
            Integer rangeId = parameterToRangeId.get(result.getParameterId());
            ReferenceRange range = rangeId == null ? null
                    : rangesById.get(rangeId);

            ReferenceRangeResponseDto rangeDto = range == null ? null
                    : new ReferenceRangeResponseDto(
                            range.getReferenceRangeId(),
                            range.getSpecies(),
                            range.getSex(),
                            range.getAgeMin(),
                            range.getAgeMax(),
                            range.getMinValue(),
                            range.getMaxValue(),
                            range.getUnit(),
                            range.getLabName());

            valueDtos.add(new ExaminationParameterResultDto(
                    result.getResultId(),
                    result.getParameterId(),
                    parameter == null ? null : parameter.getCode(),
                    parameter == null ? null : parameter.getName(),
                    parameter == null ? null : parameter.getCategory(),
                    result.getValueNumeric(),
                    result.getValueText(),
                    result.getUnit() != null ? result.getUnit()
                            : (parameter == null ? null
                                    : parameter.getDefaultUnit()),
                    result.getFlag(),
                    result.getNotes(),
                    rangeDto));
        }

        return new ExaminationResponseDto(
                examination.getExaminationId(),
                examination.getPetId(),
                typeSummary,
                examination.getExaminationDate(),
                examination.getClinicName(),
                examination.getDoctorName(),
                examination.getDiagnosis(),
                examination.getNotes(),
                valueDtos);
    }
}