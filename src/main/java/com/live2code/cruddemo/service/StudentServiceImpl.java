package com.live2code.cruddemo.service;

import com.live2code.cruddemo.dto.*;
import com.live2code.cruddemo.entity.Course;
import com.live2code.cruddemo.entity.Student;
import com.live2code.cruddemo.exception.CourseNotFoundException;
import com.live2code.cruddemo.exception.InvalidDataException;
import com.live2code.cruddemo.exception.StudentNotFoundException;
import com.live2code.cruddemo.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{

    private final StudentRepository repository;

    @Autowired
    public StudentServiceImpl(StudentRepository repository){
        this.repository = repository;
    }

    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {

        if(dto.getFirstName() == null || dto.getFirstName().isBlank())
            throw new InvalidDataException("First name required");

        if(dto.getEmail() == null || dto.getEmail().isBlank())
            throw new InvalidDataException("Email required");

        Student student = StudentMapper.toEntity(dto);

        Student saved = repository.save(student);

        return StudentMapper.toDTO(saved);
    }

    @Override
    public StudentResponseDTO getStudentById(int id) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found: " + id));

        return StudentMapper.toDTO(student);
    }

    @Override
    public List<CourseResponseDTO> getCoursesByStudentId(int studentId) {

        Student student = repository.findById(studentId)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found"));

        return student.getCourses()
                .stream()
                .map(course -> {
                    CourseResponseDTO dto = new CourseResponseDTO();
                    dto.setId(course.getCourseId());
                    dto.setTitle(course.getTitle());
                    return dto;
                })
                .toList();
    }

    @Override
    public void deleteStudent(int studentId) {

        Student student = repository.findById(studentId)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found"));

        repository.delete(student);
    }

    @Override
    public Student patchStudent(int id, Map<String, Object> payload) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found"));

        payload.forEach((key,value)-> {

            switch (key){

                case "firstName" -> {
                    if(value == null || ((String)value).isBlank())
                        throw new InvalidDataException("First name cannot be empty");

                    student.setFirstName((String) value);
                }

                case "email" -> {
                    if(value == null || ((String)value).isBlank())
                        throw new InvalidDataException("Email cannot be empty");

                    student.setEmail((String) value);
                }

                case "lastName" -> student.setLastName((String) value);

                default -> throw new InvalidDataException("Invalid field: "+key);
            }

        });

        return repository.save(student);
    }
}



