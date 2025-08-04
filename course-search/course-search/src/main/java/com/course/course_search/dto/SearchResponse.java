// File: src/main/java/com/course/course_search/dto/SearchResponse.java
package com.course.course_search.dto;

import java.util.List;

import com.course.course_search.documents.CourseDocument;

public record SearchResponse(
        long total,
        List<CourseDocument> courses
) {}