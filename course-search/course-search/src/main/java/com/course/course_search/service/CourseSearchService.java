package com.course.course_search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.course.course_search.documents.CourseDocument;
import com.course.course_search.dto.CourseSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public SearchResponse<CourseDocument> searchCourses(CourseSearchRequest request) throws Exception {
        List<Query> filters = new ArrayList<>();

        // ✅ Full-text search
        if (request.getQ() != null && !request.getQ().isEmpty()) {
            filters.add(MultiMatchQuery.of(m -> m
                    .fields("title", "description")
                    .query(request.getQ())
            )._toQuery());
        }

        // ✅ Category filter
        if (request.getCategory() != null) {
            filters.add(TermQuery.of(t -> t
                    .field("category")
                    .value(request.getCategory())
            )._toQuery());
        }

        // ✅ Type filter
        if (request.getType() != null) {
            filters.add(TermQuery.of(t -> t
                    .field("type")
                    .value(request.getType())
            )._toQuery());
        }

        // ✅ Correct Age Filter (this works)
        if (request.getMinAge() != null) {
            filters.add(RangeQuery.of(r -> r
                    .field("minAge")
                    .gte(JsonData.of(request.getMinAge()))  // ✅ This is valid
            )._toQuery());
        }

        if (request.getMaxAge() != null) {
            filters.add(RangeQuery.of(r -> r
                    .field("maxAge")
                    .lte(JsonData.of(request.getMaxAge()))
            )._toQuery());
        }

        // ✅ Price Filter
        if (request.getMinPrice() != null || request.getMaxPrice() != null) {
            filters.add(RangeQuery.of(r -> {
                r.field("price");
                if (request.getMinPrice() != null)
                    r.gte(JsonData.of(request.getMinPrice()));
                if (request.getMaxPrice() != null)
                    r.lte(JsonData.of(request.getMaxPrice()));
                return r;
            })._toQuery());
        }

        // ✅ Date Filter
        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            filters.add(RangeQuery.of(r -> r
                    .field("nextSessionDate")
                    .gte(JsonData.of(request.getStartDate()))
            )._toQuery());
        }

        // Combine all filters with bool
        Query query = filters.isEmpty()
                ? MatchAllQuery.of(m -> m)._toQuery()
                : BoolQuery.of(b -> b.must(filters))._toQuery();

        // Sorting
        List<SortOptions> sortOptions = new ArrayList<>();
        if ("priceAsc".equalsIgnoreCase(request.getSort())) {
            sortOptions.add(SortOptions.of(s -> s.field(f -> f.field("price").order(SortOrder.Asc))));
        } else if ("priceDesc".equalsIgnoreCase(request.getSort())) {
            sortOptions.add(SortOptions.of(s -> s.field(f -> f.field("price").order(SortOrder.Desc))));
        } else {
            sortOptions.add(SortOptions.of(s -> s.field(f -> f.field("nextSessionDate").order(SortOrder.Asc))));
        }

        // Pagination
        int from = request.getPage() * request.getSize();

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("courses")
                .query(query)
                .sort(sortOptions)
                .from(from)
                .size(request.getSize())
        );

        return elasticsearchClient.search(searchRequest, CourseDocument.class);
    }
}
