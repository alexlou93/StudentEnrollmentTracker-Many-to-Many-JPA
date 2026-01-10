package com.live2code.cruddemo;

import com.live2code.cruddemo.dto.CourseDTO;
import com.live2code.cruddemo.dto.StudentDTO;
import com.live2code.cruddemo.entity.Student;
import com.live2code.cruddemo.exception.InvalidDataException;
import com.live2code.cruddemo.repository.StudentRepository;
import com.live2code.cruddemo.service.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;


    private StudentDTO getValidStudentDTO() {

        StudentDTO student = new StudentDTO();

        student.setFirstName("Ben");
        student.setLastName("Gilbert");
        student.setEmail("ben.gilbert@test123.com");

        CourseDTO course1 = new CourseDTO();
        course1.setId(101);
        course1.setTitle("EVS");

        CourseDTO course2 = new CourseDTO();
        course2.setId(102);
        course2.setTitle("Sociology");

        student.setCourseList(List.of(course1,course1));
        return student;
    }


    @Test
    void saveStudent_shouldSaveStudentWithCourse(){

        StudentDTO student = getValidStudentDTO();

        Student savedStudent = new Student();
        savedStudent.setStudentId(1);

        when(studentRepository.save(any(Student.class))).
                thenReturn(savedStudent);

        StudentDTO result =  studentService.saveStudent(student);

        assertNotNull(result);
        assertEquals(1, result.getId());

        verify(studentRepository, times(1)).save(any(Student.class));
    }


    @Test
    void shouldThrowException_WhenFirstNameNull(){

        StudentDTO dto = getValidStudentDTO();
        dto.setFirstName(null);

        InvalidDataException ex = assertThrows(InvalidDataException.class,
                () -> studentService.saveStudent(dto));

        assertEquals("First name and Email are mandatory!", ex.getMessage());

        verify(studentRepository, never()).save(any(Student.class));

    }

    @Test
    void shouldThrowException_WhenFirstNameBlank() {
        StudentDTO dto = getValidStudentDTO();
        dto.setFirstName("   ");

        assertThrows(InvalidDataException.class,
                () -> studentService.saveStudent(dto));

        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void courseTitleBlankException(){

        StudentDTO dto = getValidStudentDTO();
        dto.getCourseList().getFirst().setTitle(" ");

        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> studentService.saveStudent(dto));

        assertEquals("Course title cannot be empty!", exception.getMessage());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void courseTitleNullException(){

        StudentDTO dto = getValidStudentDTO();
        dto.getCourseList().getFirst().setTitle(null);

        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> studentService.saveStudent(dto));

        assertEquals("Course title cannot be empty!", exception.getMessage());
        verify(studentRepository, never()).save(any(Student.class));
    }


}
