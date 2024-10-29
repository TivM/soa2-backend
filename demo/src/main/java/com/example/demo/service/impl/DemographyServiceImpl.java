package com.example.demo.service.impl;

import com.example.demo.client.api.PersonsClient;
import com.example.demo.service.DemographyService;
import lombok.RequiredArgsConstructor;
import org.library.dto.response.PersonResponse;
import org.library.enums.Color;
import org.library.exception.IllegalParameterException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DemographyServiceImpl implements DemographyService {

    private final PersonsClient personsClient;

    @Override
    public Long getCount(String color) {
        PersonResponse[] persons = personsClient.getAllPersons();
        List<String> colors = personsClient.getPersonEnum(color).enumValues();
        if (color.isEmpty() | !colors.contains(color.toUpperCase())) {
            throw new IllegalParameterException("color doesn't exist");
        }
        Long count = 0L;
        for (PersonResponse person : persons) {
            if (person.hairColor().name().equals(color.toUpperCase())) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Float getPercentage(String hairColor, String nationality) {
        List<String> colors = personsClient.getPersonEnum(hairColor).enumValues();
        assert hairColor != null;
        if (hairColor.isEmpty() | !colors.contains(hairColor.toUpperCase())) {
            throw new IllegalParameterException("color doesn't exist");
        }
        List<String> nationalities = personsClient.getPersonEnum(nationality).enumValues();
        assert nationality != null;
        if (nationality.isEmpty() | !nationalities.contains(nationality.toUpperCase())) {
            throw new IllegalArgumentException("nationality doesn't exist");
        }
        PersonResponse[] persons = personsClient.getAllPersons();
        int total = persons.length;
        long countByColorAndNationality = 0L;
        for (PersonResponse person : persons) {
            if (person.hairColor().name().equals(hairColor.toUpperCase())
                    && person.nationality().name().equals(nationality.toUpperCase())) {
                countByColorAndNationality++;
            }
        }
        return total != 0 ? (float) countByColorAndNationality / total * 100 : 0;
    }
}
