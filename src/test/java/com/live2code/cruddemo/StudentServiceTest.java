package com.live2code.cruddemo;

import com.live2code.cruddemo.dto.CourseResponseDTO;
import com.live2code.cruddemo.dto.StudentRequestDTO;
import com.live2code.cruddemo.dto.StudentResponseDTO;
import com.live2code.cruddemo.entity.Course;
import com.live2code.cruddemo.entity.Student;
import com.live2code.cruddemo.exception.InvalidDataException;
import com.live2code.cruddemo.exception.StudentNotFoundException;
import com.live2code.cruddemo.repository.StudentRepository;
import com.live2code.cruddemo.service.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;


    private Student createStudent() {

        Student student = new Student();
        student.setStudentId(1);
        student.setFirstName("Alex");
        student.setLastName("John");
        student.setEmail("alex@test.com");

        Course course = new Course();
        course.setCourseId(10);
        course.setTitle("Java");

        student.setCourses(List.of(course));

        return student;
    }


    @Test
    void shouldCreateStudentSuccessfully() {

        StudentRequestDTO request = new StudentRequestDTO();
        request.setFirstName("Alex");
        request.setLastName("John");
        request.setEmail("alex@test.com");

        Student student = new Student();
        student.setStudentId(1);
        student.setFirstName("Alex");
        student.setLastName("John");
        student.setEmail("alex@test.com");

        student.setCourses(new ArrayList<>());

        Mockito.when(studentRepository.save(Mockito.any(Student.class)))
                .thenReturn(student);

        StudentResponseDTO result = studentService.createStudent(request);

        assertNotNull(result);
        assertEquals("Alex", result.getFirstName());

        Mockito.verify(studentRepository, Mockito.times(1))
                .save(Mockito.any(Student.class));
    }


    @Test
    void shouldThrowExceptionWhenFirstNameMissing() {

        StudentRequestDTO request = new StudentRequestDTO();
        request.setEmail("alex@test.com");

        assertThrows(
                InvalidDataException.class,
                () -> studentService.createStudent(request)
        );
    }

    @Test
    void shouldReturnStudentById() {

        Student student = createStudent();

        Mockito.when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));

        StudentResponseDTO result = studentService.getStudentById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Alex", result.getFirstName());

        Mockito.verify(studentRepository).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenStudentNotFound() {

        Mockito.when(studentRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.getStudentById(1)
        );
    }

    @Test
    void shouldReturnCoursesByStudentId() {

        Student student = createStudent();

        Mockito.when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));

        List<CourseResponseDTO> courses =
                studentService.getCoursesByStudentId(1);

        assertEquals(1, courses.size());
        assertEquals("Java", courses.getFirst().getTitle());
    }

    @Test
    void shouldThrowExceptionWhenStudentNotFoundForCourses() {

        Mockito.when(studentRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.getCoursesByStudentId(1)
        );
    }

    @Test
    void shouldDeleteStudent() {

        Student student = createStudent();

        Mockito.when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));

        studentService.deleteStudent(1);

        Mockito.verify(studentRepository).delete(student);
    }

    @Test
    void shouldPatchStudentEmail() {

        Student student = createStudent();

        Mockito.when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));

        Mockito.when(studentRepository.save(Mockito.any(Student.class)))
                .thenReturn(student);

        Map<String,Object> payload = new HashMap<>();
        payload.put("email","updated@test.com");

        Student result = studentService.patchStudent(1,payload);

        assertEquals("updated@test.com", result.getEmail());
    }

    @Test
    void shouldThrowExceptionForInvalidPatchField() {

        Student student = createStudent();

        Mockito.when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));

        Map<String,Object> payload = new HashMap<>();
        payload.put("age",22);

        assertThrows(
                InvalidDataException.class,
                () -> studentService.patchStudent(1,payload)
        );
    }

    @Test
    void shouldThrowExceptionWhenPatchStudentNotFound() {

        Mockito.when(studentRepository.findById(1))
                .thenReturn(Optional.empty());

        Map<String,Object> payload = new HashMap<>();
        payload.put("email","test@test.com");

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.patchStudent(1,payload)
        );
    }


}
