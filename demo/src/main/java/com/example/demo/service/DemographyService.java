package com.example.demo.service;

public interface DemographyService {
    Long getCount(String color);

    Float getPercentage(String hairColor, String nationality);
}
