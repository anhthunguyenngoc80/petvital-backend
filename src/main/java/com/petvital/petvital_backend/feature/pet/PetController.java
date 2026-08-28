package com.petvital.petvital_backend.feature.pet;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petvital.petvital_backend.feature.pet.dto.PetRequestDto;
import com.petvital.petvital_backend.feature.pet.dto.PetResponseDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping("/pets")
    public ResponseEntity<PetResponseDto> registerPet(
            @Valid @RequestBody PetRequestDto request) {

        PetResponseDto response = petService.addPet(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("{ownerId}/pets")
    public ResponseEntity<List<PetResponseDto>> getPetsByOwner(
            @PathVariable Integer ownerId) {

        List<PetResponseDto> pets = petService.getPetsByOwner(ownerId);

        return ResponseEntity.ok(pets);
    }
}