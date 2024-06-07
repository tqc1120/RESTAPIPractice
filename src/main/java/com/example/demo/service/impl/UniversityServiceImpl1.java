package com.example.demo.service.impl;

import com.example.demo.domain.University;
import com.example.demo.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class UniversityServiceImpl1 implements UniversityService {

    private final RestTemplate restTemplate;
    private final ExecutorService pool;
    @Value("${university-url}")
    private String original_url;

    @Autowired
    public UniversityServiceImpl1(RestTemplate restTemplate, ExecutorService pool) {
        this.restTemplate = restTemplate;
        this.pool = pool;
    }

    @Override
    public University[] getAll() {
        return restTemplate.getForObject(original_url, University[].class);
    }

    @Override
    public List<University> getUniversitiesWithCountriesName(List<String> countries) {
        List<CompletableFuture<University[]>> completableFutures = new ArrayList<>();
        countries.forEach(country ->
                completableFutures.add(CompletableFuture.supplyAsync(() -> restTemplate.getForObject(original_url + "?country=" + country, University[].class), pool)
                )
        );
        List<University> res = new ArrayList<>();
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).
                thenAccept(Void -> completableFutures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(Arrays::stream)
                        .collect(Collectors.toList())
                ).join();
        return res;
    }
}
