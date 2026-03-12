package com.live2code.cruddemo;

import com.live2code.cruddemo.controller.StudentController;
import com.live2code.cruddemo.dto.CourseRequestDTO;
import com.live2code.cruddemo.dto.CourseResponseDTO;
import com.live2code.cruddemo.dto.StudentRequestDTO;
import com.live2code.cruddemo.dto.StudentResponseDTO;
import com.live2code.cruddemo.entity.Student;
import com.live2code.cruddemo.exception.GlobalExceptionHandler;
import com.live2code.cruddemo.exception.InvalidDataException;
import com.live2code.cruddemo.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Import(GlobalExceptionHandler.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentResponseDTO createStudentResponse() {

        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(1);
        dto.setFirstName("Alex");
        dto.setLastName("John");
        dto.setEmail("alex@test.com");

        CourseResponseDTO course = new CourseResponseDTO();
        course.setId(10);
        course.setTitle("Java");

        dto.setCourses(List.of(course));

        return dto;
    }

    @Test
    void shouldCreateStudent() throws Exception{

        StudentRequestDTO request = new StudentRequestDTO();
        request.setFirstName("Alex");
        request.setLastName("John");
        request.setEmail("alex@test.com");

        StudentResponseDTO response = createStudentResponse();

        when(studentService.createStudent(any(StudentRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Alex"))
                .andExpect(jsonPath("$.email").value("alex@test.com"));

    }

    @Test
    void shouldReturnBadRequestWhenInvalidInput() throws Exception {

        StudentRequestDTO request = new StudentRequestDTO();
        request.setEmail("wrongEmail");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnStudentById() throws Exception {

        StudentResponseDTO response = createStudentResponse();

        Mockito.when(studentService.getStudentById(1))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alex"))
                .andExpect(jsonPath("$.id").value(1));
    }


    @Test
    void shouldReturnCoursesByStudentId() throws Exception {

        CourseResponseDTO course = new CourseResponseDTO();
        course.setId(10);
        course.setTitle("Java");

        Mockito.when(studentService.getCoursesByStudentId(1))
                .thenReturn(List.of(course));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students/1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Java"));
    }

    @Test
    void shouldDeleteStudent() throws Exception {

        Mockito.doNothing().when(studentService).deleteStudent(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/students/1"))
                .andExpect(status().isOk());
    }


    @Test
    void shouldPatchStudentEmail() throws Exception {

        Student student = new Student();
        student.setStudentId(1);
        student.setEmail("updated@test.com");

        Mockito.when(studentService.patchStudent(Mockito.eq(1), Mockito.anyMap()))
                .thenReturn(student);

        Map<String,Object> payload = new HashMap<>();
        payload.put("email","updated@test.com");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestForInvalidPatchField() throws Exception {

        Mockito.when(studentService.patchStudent(Mockito.eq(1), Mockito.anyMap()))
                .thenThrow(new InvalidDataException("Invalid field"));

        Map<String,Object> payload = new HashMap<>();
        payload.put("age",22);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }
}
