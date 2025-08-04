// File: CourseRepository.java
package com.course.course_search.repository;

import com.course.course_search.documents.CourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends ElasticsearchRepository<CourseDocument, String> {
}