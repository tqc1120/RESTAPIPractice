package com.example.demo.service.impl;

import com.example.demo.domain.University;
import com.example.demo.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class UniversityServiceImpl1 implements UniversityService {
    private final RestTemplate restTemplate;
    private final ExecutorService pool;

    @Value("${university-url}")
    private String url;

    @Autowired
    public UniversityServiceImpl1(RestTemplate restTemplate, ExecutorService pool) {
        this.restTemplate = restTemplate;
        this.pool = pool;
    }

    @Override
    public University[] getAll() {
        return restTemplate.getForObject(url, University[].class);
    }

    @Override
    public List<University> getUniversitiesWithCountriesName(List<String> countries) {
        List<CompletableFuture<University[]>> completableFutures = new ArrayList<>();
        countries.forEach(c ->
                completableFutures.add(
                        CompletableFuture.supplyAsync(() -> restTemplate.getForObject(url + "?country=" + c, University[].class), pool)
                )
        );

        List<University> res = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]))
                .thenApply(Void ->
                        completableFutures.stream()
                                .map(CompletableFuture::join)
                                .flatMap(Arrays::stream)
                                .collect(Collectors.toList())
                ).join();
        return res;
    }
}
