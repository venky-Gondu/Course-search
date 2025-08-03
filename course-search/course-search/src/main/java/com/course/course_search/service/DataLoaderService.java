package com.course.course_search.service;

import com.course.course_search.documents.CourseDocument;
import com.course.course_search.repository.CourseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataLoaderService {

    private final CourseRepository courseRepository;

    @PostConstruct
    public void loadData() {
        try {
            System.out.println("🔍 Attempting to read sample-courses.json...");

            // Configure ObjectMapper to support Java 8 time (Instant)
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            InputStream inputStream = new ClassPathResource("sample-courses.json").getInputStream();
            List<CourseDocument> courses = objectMapper.readValue(inputStream, new TypeReference<>() {});

            System.out.println("📦 Loaded " + courses.size() + " courses from JSON");

            if (courseRepository.count() == 0) {
                 courseRepository.saveAll(courses); // Load only if empty
            }

            System.out.println("✅ Successfully indexed courses into Elasticsearch");

        } catch (Exception e) {
            System.err.println("❌ Error loading sample-courses.json: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
