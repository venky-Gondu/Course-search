// File: DataLoaderService.java
package com.course.course_search.service;

import com.course.course_search.documents.CourseDocument;
import com.course.course_search.repository.CourseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class DataLoaderService {

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    public DataLoaderService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @jakarta.annotation.PostConstruct
    public void loadData() {
        try {
            System.out.println("Attempting to read sample-courses.json...");
            InputStream inputStream = new ClassPathResource("sample-courses.json").getInputStream();
            var typeRef = new TypeReference<java.util.List<CourseDocument>>() {};
            java.util.List<CourseDocument> courses = objectMapper.readValue(inputStream, typeRef);

            System.out.println("Loaded " + courses.size() + " courses from JSON");

            if (courseRepository.count() == 0) {
                courseRepository.saveAll(courses);
                System.out.println("Successfully indexed " + courses.size() + " courses into Elasticsearch");
            } else {
                System.out.println("Skipped indexing: data already exists in Elasticsearch");
            }
        } catch (Exception e) {
            System.err.println("Error loading sample-courses.json: " + e.getMessage());
            throw new RuntimeException("Failed to load sample data", e);
        }
    }
}