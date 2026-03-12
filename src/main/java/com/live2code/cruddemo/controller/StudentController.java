package com.live2code.cruddemo.controller;

import com.live2code.cruddemo.dto.*;
import com.live2code.cruddemo.entity.Student;
import com.live2code.cruddemo.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    @Autowired
    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(
            @Valid @RequestBody StudentRequestDTO dto){

        return new ResponseEntity<>(
                service.createStudent(dto),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudent(@PathVariable int id){

        return ResponseEntity.ok(service.getStudentById(id));
    }


    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseResponseDTO>> getCourses(@PathVariable int id){

        return ResponseEntity.ok(service.getCoursesByStudentId(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id){

        service.deleteStudent(id);

        return ResponseEntity.ok("Student deleted");
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Student> patchStudent(
            @PathVariable int id,
            @RequestBody Map<String,Object> payload){

        return ResponseEntity.ok(service.patchStudent(id,payload));
    }
}
