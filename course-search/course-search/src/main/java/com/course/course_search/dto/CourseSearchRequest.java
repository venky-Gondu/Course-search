package com.course.course_search.dto;

import lombok.Data;


@Data
public class CourseSearchRequest {
    private String q;               // Full-text search
    private String category;        // Exact match
    private String type;            // Exact match
    private Integer minAge;
    private Integer maxAge;
    private Double minPrice;
    private Double maxPrice;
    private String startDate;       // ISO-8601 (e.g., 2025-08-10T00:00:00Z)
    private String sort;            // "upcoming", "priceAsc", "priceDesc"
    private Integer page = 0;       // Page number (0-based)
    private Integer size = 10;      // Page size
}
