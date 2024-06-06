package com.example.demo.Controller;

import com.example.demo.Entity.University;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/universities")
public class UniversityController {
    @Autowired
    private RestTemplate restTemplate;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private static final String original_url = "http://universities.hipolabs.com/search";
    private static final String uk_url = "http://universities.hipolabs.com/search?country=United+Kingdom";

    /**
     * GET to fetch all universities.
     * @return CompletableFuture containing a list of filtered universities
     */
    @GetMapping
    public CompletableFuture<List<University>> getAllUniversities() {
        return CompletableFuture.supplyAsync(() -> {
            ResponseEntity<University[]> responseEntity = restTemplate.exchange(
                    original_url, HttpMethod.GET, null, University[].class);
            University[] universities = responseEntity.getBody();
            return universities != null ? filterUniversities(universities) : new ArrayList<>();
        });
    }

    /**
     * POST to fetch universities by countries.
     * @return CompletableFuture containing a list of filtered universities in UK
     */
    @PostMapping
    public CompletableFuture<List<University>> getUniversitiesInUK() {
        return CompletableFuture.supplyAsync(() -> {
            ResponseEntity<University[]> responseEntity = restTemplate.exchange(
                    uk_url, HttpMethod.GET, null,University[].class);
            University[] universities = responseEntity.getBody();
            return universities != null ? filterUniversities(universities) : new ArrayList<>();
        });
    }

    /**
     * Filters university's info to include only name, domain, web_page.
     * @param universities array of universities
     * @return List of filtered universities
     */
    private List<University> filterUniversities(University[] universities) {
        return Arrays.stream(universities)
                .map(university -> {
                    University new_univerity = new University();
                    new_univerity.setName(university.getName());
                    new_univerity.setDomain(university.getDomain());
                    new_univerity.setWeb_pages(university.getWeb_pages());
                    return new_univerity;
                })
                .collect(Collectors.toList());
    }
}
