package com.example.demo.service;

import com.example.demo.UniversityProject;
import com.example.demo.domain.University;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UniversityService {
    University[] getAll();
    List<University> getUniversitiesWithCountriesName(List<String> countries);
}
