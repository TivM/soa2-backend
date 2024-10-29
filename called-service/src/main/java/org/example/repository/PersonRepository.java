package org.example.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.library.dto.Coordinates;
import org.library.dto.Location;
import org.library.entity.Person;
import org.library.enums.Color;
import org.library.enums.Nationality;


import java.util.List;
import java.util.Optional;

@Stateless
public class PersonRepository {

    @PersistenceContext
    private EntityManager em;

    public Person create(Person person) {
        em.persist(person);
        em.flush();
        return person;
    }

    public List<Person> findAll() {
        return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    public Optional<Person> findById(int personId) {
        return Optional.ofNullable(em.find(Person.class, personId));
    }

    public void delete(int personId) {
        Person person = findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid person Id:" + personId));
        em.remove(person);
    }

    public Person update(
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
        Person person = findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid person Id:" + personId));
        Person newPerson = new Person()
                .setId(personId)
                .setName(name)
                .setCoordinateX(coordinates.x())
                .setCoordinateY(coordinates.y())
                .setHeight(height)
                .setBirthday(birthday)
                .setPassportID(passportID)
                .setHairColor(hairColor)
                .setEyesColor(eyesColor)
                .setNationality(nationality)
                .setLocationCoordinateX(location.x())
                .setLocationCoordinateY(location.y())
                .setLocationName(location.name());
        return em.merge(newPerson);
    }
}
