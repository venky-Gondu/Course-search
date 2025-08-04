package com.course.course_search.service;

import com.course.course_search.documents.CourseDocument;
import com.course.course_search.dto.SearchRequest;
import com.course.course_search.dto.SearchResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
// import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
// import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public SearchService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public SearchResponse searchCourses(SearchRequest request) {
        Criteria criteria = new Criteria();

        // Full-text search
        if (request.getQ() != null && !request.getQ().isEmpty()) {
            criteria = criteria.and(
                Criteria.where("title").matches(request.getQ())
                    .or("description").matches(request.getQ())
            );
        }

        // Filters
        if (request.getCategory() != null) {
            criteria = criteria.and("category").is(request.getCategory());
        }
        
        if (request.getType() != null) {
            criteria = criteria.and("type").is(request.getType());
        }
        
        if (request.getMinAge() != null) {
            criteria = criteria.and("minAge").greaterThanEqual(request.getMinAge());
        }
        
        if (request.getMaxAge() != null) {
            criteria = criteria.and("maxAge").lessThanEqual(request.getMaxAge());
        }
        
        if (request.getMinPrice() != null) {
            criteria = criteria.and("price").greaterThanEqual(request.getMinPrice());
        }
        
        if (request.getMaxPrice() != null) {
            criteria = criteria.and("price").lessThanEqual(request.getMaxPrice());
        }
        
        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            criteria = criteria.and("nextSessionDate").greaterThanEqual(request.getStartDate());
        }

        // Pagination
        PageRequest pageRequest = PageRequest.of(
                request.getPage(),
                request.getSize()
        );

        // Sorting
        Sort sort = switch (request.getSort()) {
            case "priceAsc" -> Sort.by(Sort.Direction.ASC, "price");
            case "priceDesc" -> Sort.by(Sort.Direction.DESC, "price");
            default -> Sort.by(Sort.Direction.ASC, "nextSessionDate");
        };

        // Build the criteria query
        CriteriaQuery query = new CriteriaQuery(criteria)
                .setPageable(pageRequest)
                .addSort(sort);

        SearchHits<CourseDocument> hits = elasticsearchOperations.search(query, CourseDocument.class);

        return new SearchResponse(hits.getTotalHits(), hits.get().map(SearchHit::getContent).toList());
    }
}