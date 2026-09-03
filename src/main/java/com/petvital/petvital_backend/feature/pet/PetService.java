package com.petvital.petvital_backend.feature.pet;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.petvital.petvital_backend.feature.pet.dto.PetRequestDto;
import com.petvital.petvital_backend.feature.pet.dto.PetResponseDto;
import com.petvital.petvital_backend.feature.user.User;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Transactional
    public PetResponseDto addPet(PetRequestDto request, User owner) {
        Pet pet = new Pet();
        pet.setOwnerId(owner.getUserId());
        pet.setName(request.name());
        pet.setSpecies(request.species());
        pet.setBreed(request.breed());
        pet.setSex(request.sex());
        pet.setBirthDate(request.birthDate());
        pet.setWeight(request.weight());

        Pet saved = petRepository.save(pet);

        return toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public List<PetResponseDto> getAllPets() {
        return petRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PetResponseDto> getPetsByOwner(Integer ownerId) {
        return petRepository.findAllByOwnerId(ownerId).stream()
                .map(this::toResponseDto)
                .toList();
    }

    private PetResponseDto toResponseDto(Pet pet) {
        return new PetResponseDto(
                pet.getPetId(),
                pet.getOwnerId(),
                pet.getName(),
                pet.getSpecies(),
                pet.getBreed(),
                pet.getSex(),
                pet.getBirthDate(),
                pet.getWeight()
        );
    }
}