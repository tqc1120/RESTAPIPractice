package com.example.demo.Service;

import com.example.demo.Entity.University;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class UniversityService {
    @Autowired
    private RestTemplate restTemplate;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private static final String original_url = "http://universities.hipolabs.com/search";

    public CompletableFuture<List<University>> getAllUniversities() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<University[]> responseEntity = restTemplate.exchange(
                        original_url, HttpMethod.GET, null, University[].class);
                University[] universities = responseEntity.getBody();
                return universities != null ? filterUniversities(universities) : new ArrayList<>();
            } catch (RestClientException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        });
    }

    public CompletableFuture<List<University>> getUniversitiesWithCountriesName(@RequestBody List<String> countries) {
        List<CompletableFuture<List<University>>> completableFutures = countries.parallelStream().
                <CompletableFuture<List<University>>>map(country -> CompletableFuture.supplyAsync(() -> {
            String cur_url = original_url + "?country={country}";
            try {
                ResponseEntity<University[]> responseEntity = restTemplate.exchange(
                        cur_url, HttpMethod.GET, null, University[].class, country);
                University[] universities = responseEntity.getBody();
                return universities != null ? filterUniversities(universities) : new ArrayList<>();
            } catch (RestClientException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }, executor)).collect(Collectors.toList());

        CompletableFuture<Void> res = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
        return res.thenApply(v -> completableFutures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList()));
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
