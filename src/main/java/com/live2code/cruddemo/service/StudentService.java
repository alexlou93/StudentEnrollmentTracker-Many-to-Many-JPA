package com.live2code.cruddemo.service;

import com.live2code.cruddemo.dto.StudentDTO;
import com.live2code.cruddemo.entity.Student;

public interface StudentService {

    // fetch student along with associated courses using student id
    StudentDTO getStudentById(int id);

    StudentDTO saveStudent(StudentDTO studentDTO);

    String deleteStudent(int studentId);
}
