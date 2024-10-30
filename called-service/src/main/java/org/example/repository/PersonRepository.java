package org.example.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.library.dto.Coordinates;
import org.library.dto.Location;
import org.library.entity.Person;
import org.library.enums.Color;
import org.library.enums.Nationality;


import java.util.List;
import java.util.Optional;

@Stateless
public class PersonRepository {

//    @PersistenceContext(unitName = "Person")
//    private EntityManager em;

    private final EntityManagerFactory entityManagerFactory;

    public PersonRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory("Person");
    }



    public Person create(Person person) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        return person;
    }

    public List<Person> findAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();

    }

    public Optional<Person> findById(int personId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        return Optional.ofNullable(em.find(Person.class, personId));
    }

    public void delete(int personId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Person person = findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid person Id:" + personId));
        em.remove(person);
        em.close();
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
        EntityManager em = entityManagerFactory.createEntityManager();
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

    private void close() {
        entityManagerFactory.close(); // Закрываем EntityManagerFactory при завершении работы приложения
    }
}
