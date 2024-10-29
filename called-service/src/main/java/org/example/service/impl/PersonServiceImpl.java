package org.example.service.impl;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.library.dto.Coordinates;
import org.library.dto.Location;
import org.library.entity.Person;
import org.example.repository.PersonRepository;
import org.example.service.PersonService;
import org.library.enums.Color;
import org.library.enums.Nationality;
import org.library.enums.Operation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class PersonServiceImpl implements PersonService {

    @Inject
    private PersonRepository personRepository;

    @Override
    public Person add(
            String name,
            Coordinates coordinates,
            double height,
            String birthday,
            String passportID,
            Color hairColor,
            Color eyesColor,
            Nationality nationality,
            Location location
    ) {
        Person person = new Person()
                .setName(name)
                .setCoordinateX(coordinates.x())
                .setCoordinateY(coordinates.y())
                .setCreationDate(LocalDateTime.now().toString())
                .setHeight(height)
                .setBirthday(birthday)
                .setPassportID(passportID)
                .setHairColor(hairColor)
                .setEyesColor(eyesColor)
                .setNationality(nationality)
                .setLocationCoordinateX(location.x())
                .setLocationCoordinateY(location.y())
                .setLocationName(location.name());

        return personRepository.create(person);
    }

    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> getByPersonId(int personId) {
        return Optional.ofNullable(personRepository.findById(personId).orElseThrow(
                () -> new IllegalArgumentException("Invalid person Id:" + personId)
        ));
    }

    @Override
    public void deleteByPersonId(int personId) {
        personRepository.delete(personId);
    }

    @Override
    public Person updateByPersonId(
            int personId,
            String name,
            Coordinates coordinates,
            double height,
            String birthday,
            String passportID,
            Color hairColor,
            Color eyesColor,
            Nationality nationality,
            Location location
    ) {
        return personRepository.update(
                personId,
                name,
                coordinates,
                height,
                birthday,
                passportID,
                hairColor,
                eyesColor,
                nationality,
                location
        );
    }

    @Override
    public double getHeight(String operation) {
        List<Person> persons = personRepository.findAll();
        return switch (operation.toUpperCase()) {
            case "AVERAGE" -> persons.stream()
                    .mapToDouble(Person::getHeight)
                    .average()
                    .orElse(0.0);
            case "MAX" -> persons.stream()
                    .mapToDouble(Person::getHeight)
                    .max()
                    .orElse(0.0);
            case "MIN" -> persons.stream()
                    .mapToDouble(Person::getHeight)
                    .min()
                    .orElse(0.0);
            default -> throw new RuntimeException("Operation doesn't exist");
        };
    }

    @Override
    public List<String> getPersonEnum(String enumName) {
        return switch (enumName.toUpperCase()){
            case "COLOR" -> Stream.of(Color.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
            case "NATIONALITY" -> Stream.of(Nationality.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
            case "OPERATION" -> Stream.of(Operation.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
            default -> throw new IllegalStateException("Unexpected value: " + enumName.toUpperCase());
        };
    }
}
