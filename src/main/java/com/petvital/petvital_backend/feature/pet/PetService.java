package com.petvital.petvital_backend.feature.pet;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petvital.petvital_backend.feature.pet.dto.PetResponseDto;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Transactional(readOnly = true)
    public List<PetResponseDto> getAllPets() {
        return petRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PetResponseDto> getPetsByOwner(Integer ownerId) {
        return petRepository.findAllByOwner_UserId(ownerId).stream()
                .map(this::toResponseDto)
                .toList();
    }

    private PetResponseDto toResponseDto(Pet pet) {
        return new PetResponseDto(
                pet.getPetId(),
                pet.getOwner().getUserId(),
                pet.getName(),
                pet.getSpecies(),
                pet.getBreed(),
                pet.getSex(),
                pet.getBirthDate(),
                pet.getWeight()
        );
    }
}