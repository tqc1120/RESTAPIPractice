package com.example.demo.controller;

import com.example.demo.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/universities")
public class UniversityController {

    private final UniversityService universityService;

    @Autowired
    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUniversities() {
        return new ResponseEntity<>(universityService.getAll(), HttpStatus.OK);
    }

    @GetMapping(params = "countries")
    public ResponseEntity<?> getUniversitiesWithCountriesName(@RequestParam("countries") List<String> countries) {
        return new ResponseEntity<>(universityService.getUniversitiesWithCountriesName(countries), HttpStatus.OK);
    }
}
