package com.live2code.cruddemo.controller;

import com.live2code.cruddemo.dto.StudentDTO;
import com.live2code.cruddemo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StudentCourseController {

    private StudentService studentService;

    @Autowired
    public StudentCourseController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/student/{studentId}")
    ResponseEntity<StudentDTO>  fetchStudent(@PathVariable int studentId){

        StudentDTO result = studentService.getStudentById(studentId);

        return ResponseEntity.ok(result);

    }

    @PostMapping("/student")
    ResponseEntity<StudentDTO> saveStudent(@RequestBody StudentDTO studentDTO){

        StudentDTO result = studentService.saveStudent(studentDTO);

        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }

    @DeleteMapping("/student/{studentId}")
    ResponseEntity<String> deleteStudent(@PathVariable int studentId){

        studentService.deleteStudent(studentId);

        return ResponseEntity.ok("Student deleted successfully : " + studentId);
    }
}
