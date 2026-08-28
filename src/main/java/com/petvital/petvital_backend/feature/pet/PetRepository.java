package com.petvital.petvital_backend.feature.pet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Integer> {

    List<Pet> findAllByOwner_UserId(Integer ownerId);
}