package com.live2code.cruddemo.service;

import com.live2code.cruddemo.dto.*;
import com.live2code.cruddemo.entity.Student;

import java.util.List;
import java.util.Map;

public interface StudentService {


    StudentResponseDTO createStudent(StudentRequestDTO dto);

    StudentResponseDTO getStudentById(int id);

    List<CourseResponseDTO> getCoursesByStudentId(int studentId);

    void deleteStudent(int studentId);

    Student patchStudent(int id, Map<String,Object> payload);


}
