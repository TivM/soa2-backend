//package org.example.service.impl;
//
//import jakarta.ejb.Stateless;
//import org.library.dto.*;
//import org.library.entity.Person;
//import org.example.repository.PersonRepository;
//import org.example.service.PersonService;
//import org.library.enums.Color;
//import org.library.enums.FilteringOperation;
//import org.library.enums.Nationality;
//import org.library.enums.Operation;
//import org.library.exception.IllegalParameterException;
//import org.library.exception.ResourceNotFoundException;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@Stateless
//public class PersonServiceImpl implements PersonService {
//
//    private final PersonRepository personRepository;
//
//    public PersonServiceImpl() {
//        this.personRepository = new PersonRepository();
//    }
//
//    @Override
//    public Person add(
//            String name,
//            Coordinates coordinates,
//            double height,
//            String birthday,
//            String passportID,
//            Color hairColor,
//            Color eyesColor,
//            Nationality nationality,
//            Location location
//    ) {
//        Person person = new Person()
//                .setName(name)
//                .setCoordinateX(coordinates.x())
//                .setCoordinateY(coordinates.y())
//                .setCreationDate(LocalDateTime.now().toString())
//                .setHeight(height)
//                .setBirthday(birthday)
//                .setPassportID(passportID)
//                .setHairColor(hairColor)
//                .setEyesColor(eyesColor)
//                .setNationality(nationality)
//                .setLocationCoordinateX(location.x())
//                .setLocationCoordinateY(location.y())
//                .setLocationName(location.name());
//
//        return personRepository.create(person);
//    }
//
//    @Override
//    public List<Person> getAll() {
//        return personRepository.findAll();
//    }
//
//    @Override
//    public Optional<Person> getByPersonId(Integer personId) {
//        return Optional.ofNullable(personRepository.findById(personId).orElseThrow(
//                () -> new ResourceNotFoundException("Invalid person Id:" + personId)
//        ));
//    }
//
//    @Override
//    public void deleteByPersonId(Integer personId) {
//        Person person = personRepository.findById(personId).orElseThrow(
//                () -> new ResourceNotFoundException("Person with id " + personId + " doesn't exist")
//        );
//        personRepository.delete(person.getId());
//    }
//
//    @Override
//    public Person updateByPersonId(
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
//    ) {
//        Person person = personRepository.findById(personId).orElseThrow(
//                () -> new ResourceNotFoundException("Person with id " + personId + " doesn't exist")
//        );
//        return personRepository.update(
//                person.getId(),
//                name,
//                coordinates,
//                height,
//                birthday,
//                passportID,
//                hairColor,
//                eyesColor,
//                nationality,
//                location
//        );
//    }
//
//    @Override
//    public double getHeight(String operation) {
//        List<Person> persons = personRepository.findAll();
//        return switch (operation.toLowerCase()) {
//            case "average" -> persons.stream()
//                    .mapToDouble(Person::getHeight)
//                    .average()
//                    .orElse(0.0);
//            case "max" -> persons.stream()
//                    .mapToDouble(Person::getHeight)
//                    .max()
//                    .orElse(0.0);
//            case "min" -> persons.stream()
//                    .mapToDouble(Person::getHeight)
//                    .min()
//                    .orElse(0.0);
//            default -> throw new IllegalParameterException("Operation doesn't exist");
//        };
//    }
//
//    @Override
//    public List<String> getPersonEnum(String enumName) {
//        return switch (enumName.toLowerCase()){
//            case "color" -> Stream.of(Color.values())
//                    .map(Enum::name)
//                    .collect(Collectors.toList());
//            case "nationality" -> Stream.of(Nationality.values())
//                    .map(Enum::name)
//                    .collect(Collectors.toList());
//            case "operation" -> Stream.of(Operation.values())
//                    .map(Enum::name)
//                    .collect(Collectors.toList());
//            default -> throw new IllegalParameterException("Unexpected value: " + enumName.toUpperCase());
//        };
//    }
//
//
//    @Override
//    public Page<Person> getPersonsFilter(List<String> sortsList, List<String> filtersList, Integer page, Integer pageSize) {
//        if (page != null || pageSize != null){
//            if (page == null){
//                page = 1;
//            }
//            if (pageSize == null){
//                pageSize = 20;
//            }
//        }
//
//        Pattern nestedFieldNamePattern = Pattern.compile("(.*)\\.(.*)");
//        Pattern lhsPattern = Pattern.compile("(.*)\\[(.*)\\]=(.*)");
//
//        List<Sort> sorts = new ArrayList<>();
//
//        if (!sortsList.isEmpty()){
//            boolean containsOppositeSorts = sortsList.stream().allMatch(e1 ->
//                    sortsList.stream().allMatch(e2 -> Objects.equals(e1, "-" + e2))
//            );
//
//            if (containsOppositeSorts){
//                throw new IllegalParameterException("Request contains opposite sort parameters");
//            }
//
//            for (String sort: sortsList){
//                boolean desc = sort.startsWith("-");
//                String sortFieldName = desc ? sort.split("-")[1] : sort;
//                String nestedName = null;
//
//                Matcher matcher = nestedFieldNamePattern.matcher(sortFieldName);
//
//                if (matcher.find()){
//                    String nestedField = matcher.group(2).substring(0, 1).toLowerCase() + matcher.group(2).substring(1);
//                    sortFieldName = matcher.group(1);
//                    nestedName = nestedField;
//                }
//
//                sorts.add(new Sort(desc, sortFieldName, nestedName));
//            }
//        }
//
//        List<Filter> filters = new ArrayList<>();
//
//        for (String filter : filtersList){
//            Matcher matcher = lhsPattern.matcher(filter);
//            String fieldName = null, fieldValue = null;
//            FilteringOperation filteringOperation = null;
//            String nestedName = null;
//
//            if (matcher.find()){
//                fieldName = matcher.group(1);
//
//                Matcher nestedFieldMatcher = nestedFieldNamePattern.matcher(fieldName);
//                if (nestedFieldMatcher.find()){
//                    String nestedField = nestedFieldMatcher.group(2).substring(0, 1).toLowerCase() + nestedFieldMatcher.group(2).substring(1);
//                    fieldName = nestedFieldMatcher.group(1);
//                    nestedName = nestedField;
//                }
//
//                filteringOperation = FilteringOperation.fromValue(matcher.group(2));
//
//                boolean isEnum = Objects.equals(fieldName, "hairColor") || Objects.equals(fieldName, "eyesColor") || Objects.equals(fieldName, "nationality");
//                if (isEnum){
//                    if (!Objects.equals(filteringOperation, FilteringOperation.EQ) &&
//                            !Objects.equals(filteringOperation, FilteringOperation.NEQ)) {
//                        throw new IllegalParameterException("Only [eq] and [neq] operations are allowed for field");
//                    }
//                }
//
//                if (isEnum){
//                    fieldValue = matcher.group(3).toLowerCase();
//                } else fieldValue = matcher.group(3);
//            }
//
//            if (fieldName == null || fieldName.isEmpty()){
//                throw new IllegalParameterException("Filter field name is empty");
//            }
//
//            if (fieldValue == null || fieldValue.isEmpty()){
//                throw new IllegalParameterException("Filter field value is empty");
//            }
//
//            if (Objects.equals(filteringOperation, FilteringOperation.UNDEFINED)){
//                throw new IllegalParameterException("No or unknown filtering operation. Possible values are: eq,neq,gt,lt,gte,lte.");
//            }
//
//            filters.add(new Filter(fieldName, nestedName, filteringOperation, fieldValue));
//        }
//
//        Page<Person> entityPage;
//
//        try {
//            entityPage = personRepository.getSortedAndFilteredPage(sorts, filters, page, pageSize);
//        } catch (NullPointerException e){
//            throw new IllegalParameterException("Error while getting page. Check query params format. " + e.getMessage());
//        }
//
//        return new Page<>(
//                entityPage.objects(),
//                entityPage.page(),
//                entityPage.pageSize(),
//                entityPage.totalPages(),
//                entityPage.totalCount()
//        );
//    }
//
//}
