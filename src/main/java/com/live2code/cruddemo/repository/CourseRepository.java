package com.live2code.cruddemo.repository;

import com.live2code.cruddemo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
