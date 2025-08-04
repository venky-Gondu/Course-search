// File: src/main/java/com/course/course_search/dto/SearchRequest.java
package com.course.course_search.dto;

import lombok.Data;

@Data
public class SearchRequest {
    private String q;
    private Integer minAge;
    private Integer maxAge;
    private String category;
    private String type;
    private Double minPrice;
    private Double maxPrice;
    private String startDate;
    private String sort = "upcoming"; // default
    private int page = 0;
    private int size = 10;
}