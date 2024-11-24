package org.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.library.enums.Color;
import org.library.enums.Nationality;


@Getter
@Setter
@Entity
@Table(name = "person")
@Accessors(chain = true)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", allocationSize = 1, sequenceName = "person_seq")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "coordinate_x")
    private Float coordinateX;

    @Column(name = "coordinate_y")
    private Integer coordinateY;

    @Column(name = "creation_date")
    private String creationDate;

    @Column(name = "height")
    private Double height;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "passport_id")
    private String passportID;

    @Column(name = "hair_color")
    @Enumerated(EnumType.STRING)
    private Color hairColor;

    @Column(name = "eyes_color")
    @Enumerated(EnumType.STRING)
    private Color eyesColor;

    @Column(name = "nationality")
    @Enumerated(EnumType.STRING)
    private Nationality nationality;

    @Column(name = "location_coordinate_x")
    private Float locationCoordinateX;

    @Column(name = "location_coordinate_y")
    private Integer locationCoordinateY;

    @Column(name = "location_name")
    private String locationName;
}
