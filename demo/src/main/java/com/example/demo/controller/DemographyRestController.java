package com.example.demo.controller;

import com.example.demo.service.DemographyService;
import lombok.RequiredArgsConstructor;
import org.library.dto.response.CountResponse;
import org.library.dto.response.PercentageResponse;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/demography")
@RequiredArgsConstructor
public class DemographyRestController {

    private final DemographyService demographyService;

    @GetMapping("/eye-color/{eyeColor}")
    public CountResponse getCountPersonsByEyeColor(@PathVariable String eyeColor){
        Long count = demographyService.getCount(eyeColor);
        return new CountResponse(count);
    }

    @GetMapping("/nationality/{nationality}/hair-color/{hairColor}/percentage")
    public PercentageResponse getPercentagePersonsByEyeColor(@PathVariable String nationality, @PathVariable String hairColor){
        Float percentage = demographyService.getPercentage(hairColor, nationality);
        return new PercentageResponse(percentage);
    }

    @GetMapping("/get")
    public String getString(){
        return "ABOBA";
    }
}
