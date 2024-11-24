//package org.example.repository;
//
//import jakarta.ejb.Stateless;
//import jakarta.persistence.*;
//import jakarta.persistence.criteria.*;
//import jakarta.ws.rs.core.Response;
//import org.library.dto.*;
//import org.library.dto.response.AvgHeightResponse;
//import org.library.dto.response.MaxHeightResponse;
//import org.library.dto.response.MinHeightResponse;
//import org.library.entity.Person;
//import org.library.enums.Color;
//import org.library.enums.Nationality;
//import org.library.enums.Operation;
//import org.library.exception.IllegalParameterException;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//@Stateless
//public class PersonRepository {
//
//    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Person");
//
//    public EntityManager getEntityManager() {
//        return entityManagerFactory.createEntityManager();
//    }
//
//
//
//    public Person create(Person person) {
//        EntityManager em = getEntityManager();
//        try {
//            em.getTransaction().begin();
//            em.persist(person);
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            if (em.getTransaction().isActive()) {
//                em.getTransaction().rollback();
//            }
//        } finally {
//            em.close();
//        }
//        return person;
//    }
//
//    public List<Person> findAll() {
//        EntityManager em = getEntityManager();
//        List<Person> personList = new ArrayList<>();
//        try {
//            em.getTransaction().begin();
//            personList = em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            if (em.getTransaction().isActive()) {
//                em.getTransaction().rollback();
//            }
//        } finally {
//            em.close();
//        }
//        return personList;
//    }
//
//    public Optional<Person> findById(Integer personId) {
//        EntityManager em = getEntityManager();
//        Person person = null;
//        try {
//            em.getTransaction().begin();
//            person = em.find(Person.class, personId);
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            if (em.getTransaction().isActive()) {
//                em.getTransaction().rollback();
//            }
//        } finally {
//            em.close();
//        }
//        return Optional.ofNullable(person);
//    }
//
//    public void delete(Integer personId) {
//        EntityManager em = getEntityManager();
//        try {
//            em.getTransaction().begin();
//            Person person = em.find(Person.class, personId);
//            em.remove(person);
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            if (em.getTransaction().isActive()) {
//                em.getTransaction().rollback();
//            }
//        } finally {
//            em.close();
//        }
//    }
//
//    public Person update(
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
//        EntityManager em = getEntityManager();
//        Person updatedPerson = null;
//        try {
//            em.getTransaction().begin();
//            updatedPerson = em.find(Person.class, personId);
//            updatedPerson
//                    .setId(personId)
//                    .setName(name)
//                    .setCoordinateX(coordinates.x())
//                    .setCoordinateY(coordinates.y())
//                    .setHeight(height)
//                    .setBirthday(birthday)
//                    .setPassportID(passportID)
//                    .setHairColor(hairColor)
//                    .setEyesColor(eyesColor)
//                    .setNationality(nationality)
//                    .setLocationCoordinateX(location.x())
//                    .setLocationCoordinateY(location.y())
//                    .setLocationName(location.name());
//            em.merge(updatedPerson);
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            if (em.getTransaction().isActive()) {
//                em.getTransaction().rollback();
//            }
//        } finally {
//            em.close();
//        }
//        return updatedPerson;
//    }
//
//
//    public Page<Person> getSortedAndFilteredPage(List<Sort> sortList, List<Filter> filtersList, Integer page, Integer size) {
//        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<Person> flatQuery = criteriaBuilder.createQuery(Person.class);
//        Root<Person> root = flatQuery.from(Person.class);
//
//        CriteriaQuery<Person> select = flatQuery.select(root);
//
//        List<Predicate> predicates = new ArrayList<>();
//
//        if (!filtersList.isEmpty()){
//            predicates = new ArrayList<>();
//
//            for (Filter filter : filtersList){
//
//                switch (filter.filteringOperation()){
//                    case EQ:
//                        if (filter.nestedName() != null) {
//                            predicates.add(criteriaBuilder.equal(
//                                            root.get(resolveNestedName(filter.fieldName(), filter.nestedName())),
//                                            getTypedFieldValue(filter.fieldName(), filter.fieldValue())
//                                    )
//                            );
//                        } else {
//                            predicates.add(criteriaBuilder.equal(
//                                            root.get(filter.fieldName()),
//                                            getTypedFieldValue(filter.fieldName(), filter.fieldValue())
//                                    )
//                            );
//                        }
//                        break;
//                    case NEQ:
//                        if (filter.nestedName() != null) {
//                            predicates.add(criteriaBuilder.notEqual(
//                                            root.get(resolveNestedName(filter.fieldName(), filter.nestedName())),
//                                            getTypedFieldValue(filter.fieldName(), filter.fieldValue())
//                                    )
//                            );
//                        } else {
//                            predicates.add(criteriaBuilder.notEqual(
//                                            root.get(filter.fieldName()),
//                                            getTypedFieldValue(filter.fieldName(), filter.fieldValue())
//                                    )
//                            );
//                        }
//                        break;
//                    case GT:
//                        if (filter.nestedName() != null) {
//                            predicates.add(criteriaBuilder.greaterThan(
//                                            root.get(resolveNestedName(filter.fieldName(), filter.nestedName())),
//                                            filter.fieldValue()
//                                    )
//                            );
//                        } else {
//                            predicates.add(criteriaBuilder.greaterThan(
//                                            root.get(filter.fieldName()),
//                                            filter.fieldValue()
//                                    )
//                            );
//                        }
//                        break;
//                    case LT:
//                        if (filter.nestedName() != null) {
//                            predicates.add(criteriaBuilder.lessThan(
//                                            root.get(resolveNestedName(filter.fieldName(), filter.nestedName())),
//                                            filter.fieldValue()
//                                    )
//                            );
//                        } else {
//                            predicates.add(criteriaBuilder.lessThan(
//                                            root.get(filter.fieldName()),
//                                            filter.fieldValue()
//                                    )
//                            );
//                        }
//                        break;
//                    case GTE:
//                        if (filter.nestedName() != null) {
//                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(
//                                            root.get(resolveNestedName(filter.fieldName(), filter.nestedName())),
//                                            filter.fieldValue()
//                                    )
//                            );
//                        } else {
//                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(
//                                            root.get(filter.fieldName()),
//                                            filter.fieldValue()
//                                    )
//                            );
//                        }
//                        break;
//                    case LTE:
//                        if (filter.nestedName() != null){
//                            predicates.add(criteriaBuilder.lessThanOrEqualTo(
//                                            root.get(resolveNestedName(filter.fieldName(), filter.nestedName())),
//                                            filter.fieldValue()
//                                    )
//                            );
//                        } else {
//                            predicates.add(criteriaBuilder.lessThanOrEqualTo(
//                                            root.get(filter.fieldName()),
//                                            filter.fieldValue()
//                                    )
//                            );
//                        }
//                        break;
//                    case UNDEFINED:
//                        break;
//                }
//            }
//
//            select.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
//        }
//
//        if (!sortList.isEmpty()){
//            List<Order> orderList = new ArrayList<>();
//
//            for (Sort sortItem : sortList){
//                if (sortItem.desc()){
//                    if (sortItem.nestedName() != null){
//                        orderList.add(criteriaBuilder.desc(root.get(resolveNestedName(sortItem.fieldName(), sortItem.nestedName()))));
//                    } else {
//                        orderList.add(criteriaBuilder.desc(root.get(sortItem.fieldName())));
//                    }
//                } else {
//                    if (sortItem.nestedName() != null){
//                        orderList.add(criteriaBuilder.asc(root.get(resolveNestedName(sortItem.fieldName(), sortItem.nestedName()))));
//                    } else {
//                        orderList.add(criteriaBuilder.asc(root.get(sortItem.fieldName())));
//                    }
//                }
//            }
//            select.orderBy(orderList);
//        }
//
//        TypedQuery<Person> typedQuery = getEntityManager().createQuery(select);
//
//        //Page<Person> ret = new Page<>(List.of(), 0, 0,0,0L);
//
//        if (page != null && size != null){
//            typedQuery.setFirstResult((page - 1) * size);
//            typedQuery.setMaxResults(size);
//
//            long countResult = 0;
//
//            if (!predicates.isEmpty()){
//                Query queryTotal = getEntityManager().createQuery("SELECT COUNT(p.id) FROM Person p");
//                countResult = (long) queryTotal.getSingleResult();
//            } else {
//                CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
//                CriteriaQuery<Long> cq = qb.createQuery(Long.class);
//                cq.select(qb.count(cq.from(Person.class)));
//                cq.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
//                countResult = getEntityManager().createQuery(cq).getSingleResult();
//            }
//
//           return new Page<>(
//                    typedQuery.getResultList(),
//                    page,
//                    size,
//                    (int) Math.ceil((countResult * 1.0) / size),
//                    (long) typedQuery.getResultList().size()
//            );
//        }
//
//        return new Page<>(
//                typedQuery.getResultList(),0, 0, 0, (long) typedQuery.getResultList().size());
//    }
//
//
//    private Object getTypedFieldValue(String fieldName, String fieldValue) {
//        if (Objects.equals(fieldName, "hairColor")) {
//            return Color.fromValue(fieldValue);
//        } else if (Objects.equals(fieldName, "eyesColor")) {
//            return Color.fromValue(fieldValue);
//        } else if (Objects.equals(fieldName, "nationality")) {
//            return Nationality.fromValue(fieldValue);
//        } else if (Objects.equals(fieldName, "operation")) {
//            return Operation.fromValue(fieldValue);
//        } else return fieldValue;
//    }
//
//    private String resolveNestedName(String fieldName, String nestedName){
//        if (fieldName.equalsIgnoreCase("location")){
//            return switch (nestedName.toLowerCase()) {
//                case "x" -> "locationCoordinateX";
//                case "y" -> "locationCoordinateY";
//                case "name" -> "locationName";
//                default -> throw new IllegalParameterException("Field doesn't exist");
//            };
//        }
//        return switch (nestedName.toLowerCase()) {
//            case "x" -> "coordinateX";
//            case "y" -> "coordinateY";
//            default -> throw new IllegalParameterException("Field doesn't exist");
//        };
//    }
//
//}
