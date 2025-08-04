// File: CourseSearchController.java
package com.course.course_search.controller;

import com.course.course_search.dto.SearchRequest;
import com.course.course_search.dto.SearchResponse;
import com.course.course_search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class CourseSearchController {

    private final SearchService searchService;

    @GetMapping
    public SearchResponse search(
            String q,
            Integer minAge,
            Integer maxAge,
            String category,
            String type,
            Double minPrice,
            Double maxPrice,
            String startDate,
            String sort,
            Integer page,
            Integer size
    ) {
        SearchRequest request = new SearchRequest();
        request.setQ(q);
        request.setMinAge(minAge);
        request.setMaxAge(maxAge);
        request.setCategory(category);
        request.setType(type);
        request.setMinPrice(minPrice);
        request.setMaxPrice(maxPrice);
        request.setStartDate(startDate);
        request.setSort(sort != null ? sort : "upcoming");
        request.setPage(page != null ? page : 0);
        request.setSize(size != null ? size : 10);

        return searchService.searchCourses(request);
    }
}