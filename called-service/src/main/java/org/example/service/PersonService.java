//package org.example.service;
//
//import org.library.dto.Coordinates;
//import org.library.dto.Location;
//import org.library.dto.Page;
//import org.library.entity.Person;
//import org.library.enums.Color;
//import org.library.enums.Nationality;
//
//
//import java.util.List;
//import java.util.Optional;
//
//public interface PersonService {
//
//    Person add(String name,
//               Coordinates coordinates,
//               double height,
//               String birthday,
//               String passportID,
//               Color hairColor,
//               Color eyesColor,
//               Nationality nationality,
//               Location location
//    );
//    List<Person> getAll();
//    Optional<Person> getByPersonId(Integer personId);
//    void deleteByPersonId(Integer personId);
//    Person updateByPersonId(
//            Integer personId,
//            String name,
//            Coordinates coordinates,
//            double height,
//            String birthday,
//            String passportID,
//            Color hairColor,
//            Color eyesColor,
//            Nationality nationality,
//            Location location
//    );
//
//    double getHeight(String operation);
//
//    List<String> getPersonEnum(String enumName);
//
//    Page<Person> getPersonsFilter(List<String> sortsList, List<String> filtersList, Integer page, Integer pageSize);
//}
