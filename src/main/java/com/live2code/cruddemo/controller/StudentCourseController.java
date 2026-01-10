package com.live2code.cruddemo.controller;

import com.live2code.cruddemo.dto.CourseDTO;
import com.live2code.cruddemo.dto.StudentDTO;
import com.live2code.cruddemo.entity.Student;
import com.live2code.cruddemo.exception.InvalidDataException;
import com.live2code.cruddemo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StudentCourseController {

    private StudentService studentService;

    private JsonMapper jsonMapper;

    @Autowired
    public StudentCourseController(StudentService studentService, JsonMapper jsonMapper) {
        this.studentService = studentService;
        this.jsonMapper = jsonMapper;
    }

    @GetMapping("/student/{studentId}")
    ResponseEntity<StudentDTO>  fetchStudent(@PathVariable int studentId){

        StudentDTO result = studentService.getStudentById(studentId);

        return ResponseEntity.ok(result);

    }

    @GetMapping("/student/course/{studentId}")
    ResponseEntity<List<CourseDTO>> getCourseByStudentId(@PathVariable int studentId){

        List<CourseDTO> courseDTOList = studentService.getCourseByStudentId(studentId);

        return new ResponseEntity<>(courseDTOList, HttpStatus.OK);

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


    @PatchMapping("/student/{studentId}")
    ResponseEntity<String> patchStudent(@PathVariable int studentId,
                                            @RequestBody Map<String, Object> patchPayload){

        Student student = studentService.fetchStudent(studentId);

        // throw exception if request body contains "id" key
         if (patchPayload.containsKey("id")){
            throw  new InvalidDataException("Student id not allowed in request body - " + studentId);
        }

        Student patchedStudent = jsonMapper.updateValue(student, patchPayload);

        Student dbStudent = studentService.patchAndSaveStudent(patchedStudent);

        return ResponseEntity.ok("Updated successfully");
    }
}
