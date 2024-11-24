package com.example.demo.repository;

import org.library.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemographyRepository extends JpaRepository<Person, Integer> {
}
