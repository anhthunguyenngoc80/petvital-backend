package com.petvital.petvital_backend.feature.pet;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petvital.petvital_backend.feature.pet.dto.PetResponseDto;

@RestController
@RequestMapping("/api")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("{ownerId}/pets")
    public ResponseEntity<List<PetResponseDto>> getPetsByƠwner(
            @PathVariable Integer ownerId) {

        List<PetResponseDto> pets = petService.getPetsByOwner(ownerId);

        return ResponseEntity.ok(pets);
    }
}