package com.live2code.cruddemo.dto;

import com.live2code.cruddemo.entity.Course;
import com.live2code.cruddemo.entity.Student;

import java.util.List;

public class StudentMapper {

    public static StudentResponseDTO toDTO(Student student) {

        StudentResponseDTO dto = new StudentResponseDTO();

        dto.setId(student.getStudentId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());

        List<CourseResponseDTO> courses =
                student.getCourses()
                        .stream()
                        .map(course -> {
                            CourseResponseDTO c = new CourseResponseDTO();
                            c.setId(course.getCourseId());
                            c.setTitle(course.getTitle());
                            return c;
                        })
                        .toList();

        dto.setCourses(courses);

        return dto;
    }

    public static Student toEntity(StudentRequestDTO dto) {

        Student student = new Student();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());

        if (dto.getCourses() != null) {

            List<Course> courses =
                    dto.getCourses()
                            .stream()
                            .map(courseDTO -> {
                                Course c = new Course();
                                c.setTitle(courseDTO.getTitle());
                                return c;
                            })
                            .toList();

            courses.forEach(student::addCourse);
        }

        return student;
    }
}
