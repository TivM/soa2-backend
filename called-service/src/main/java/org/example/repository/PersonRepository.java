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


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
public class PersonRepository {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Person");

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }



    public Person create(Person person) {
        EntityManager em = getEntityManager();
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
        EntityManager em = getEntityManager();
        List<Person> personList = new ArrayList<>();
        try {
            em.getTransaction().begin();
            personList = em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        return personList;
    }

    public Optional<Person> findById(Integer personId) {
        EntityManager em = getEntityManager();
        Person person = null;
        try {
            em.getTransaction().begin();
            person = em.find(Person.class, personId);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        return Optional.ofNullable(person);
    }

    public void delete(Integer personId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Person person = em.find(Person.class, personId);
            if (person == null){
                throw new IllegalArgumentException("Invalid person Id:" + personId);
            }
            em.remove(person);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    public Person update(
            Integer personId,
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
        EntityManager em = getEntityManager();
        Person updatedPerson = null;
        try {
            em.getTransaction().begin();
            updatedPerson = em.find(Person.class, personId);
            if (updatedPerson == null){
                throw new IllegalArgumentException("Invalid person Id:" + personId);
            }
            updatedPerson
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
            em.merge(updatedPerson);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }

        return updatedPerson;
    }
}
