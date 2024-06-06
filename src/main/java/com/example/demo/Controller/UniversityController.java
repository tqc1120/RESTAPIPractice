package com.example.demo.Controller;

import com.example.demo.Entity.University;
import com.example.demo.Service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/universities")
public class UniversityController {
    @Autowired
    private UniversityService universityService;

    /**
     * GET to fetch all universities.
     * @return CompletableFuture containing a list of filtered universities
     */
    @GetMapping
    public CompletableFuture<List<University>> getAllUniversities() {
        return universityService.getAllUniversities();
    }

    /**
     * POST to fetch universities by countries.
     * @param countries List of country's name
     * @return CompletableFuture containing a list of filtered universities in input countries
     */
    @PostMapping
    public CompletableFuture<List<University>> getUniversitiesWithCountriesName(@RequestBody List<String> countries) {
        return universityService.getUniversitiesWithCountriesName(countries);
    }
}
