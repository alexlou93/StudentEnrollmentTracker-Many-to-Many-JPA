package com.live2code.cruddemo.service;

import com.live2code.cruddemo.dto.CourseDTO;
import com.live2code.cruddemo.dto.StudentDTO;
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
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{

    private static final Logger logger =
            LoggerFactory.getLogger(StudentServiceImpl.class);

    private StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    // save student with course details
    @Override
    public StudentDTO saveStudent(StudentDTO studentDTO) {

        logger.info("Saving student");
        logger.debug("StudentDTO received: {}", studentDTO);

        if (studentDTO.getFirstName() == null || studentDTO.getFirstName().isBlank() ||
                studentDTO.getEmail() == null || studentDTO.getEmail().isBlank()) {
            logger.warn("First name or Email is missing for student: {}", studentDTO);
            throw new InvalidDataException("First name and Email are mandatory!");
        }

        try {
            Student student = new Student();
            student.setFirstName(studentDTO.getFirstName());
            student.setLastName(studentDTO.getLastName());
            student.setEmail(studentDTO.getEmail());

            if (studentDTO.getCourseList() != null) {
                logger.debug("Adding {} courses to student",
                        studentDTO.getCourseList().size());

                for (CourseDTO dto : studentDTO.getCourseList()) {

                    if (dto.getTitle() == null || dto.getTitle().isBlank()) {
                        logger.warn("Invalid course title: {}", dto);
                        throw new InvalidDataException("Course title cannot be empty!");
                    }

                    Course course = new Course();
                    course.setTitle(dto.getTitle());
                    student.addCourse(course);
                }
            }

            Student savedStudent = studentRepository.save(student);
            logger.info("Student saved successfully with ID: {}",
                    savedStudent.getStudentId());

            studentDTO.setId(savedStudent.getStudentId());

            return studentDTO;

        } catch (Exception ex) {
            logger.error("Error while saving student", ex);
            throw ex;
        }
    }

    // fetch student along with courses
    @Override
    public StudentDTO getStudentById(int StudentId) {

        Student student = studentRepository.findById(StudentId).
                orElseThrow(()-> new StudentNotFoundException("Student not available : " + StudentId));

        try {
            StudentDTO studentDTO = new StudentDTO();

            studentDTO.setId(student.getStudentId());
            studentDTO.setFirstName(student.getFirstName());
            studentDTO.setLastName(student.getLastName());
            studentDTO.setEmail(student.getEmail());

            List<CourseDTO> courseDTOS = new ArrayList<>();

            if (student.getCourses() != null) {
                for (Course course : student.getCourses()) {
                    CourseDTO dto = new CourseDTO();
                    dto.setId(course.getCourseId());
                    dto.setTitle(course.getTitle());
                    courseDTOS.add(dto);
                }
            }

            studentDTO.setCourseList(courseDTOS);

            return studentDTO;
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong!!");
        }
    }

    // remove student but not courses

    @Override
    public String deleteStudent(int studentId) {

        Student student = studentRepository.findById(studentId).
                orElseThrow(() -> new StudentNotFoundException("Student not available : " + studentId));

        studentRepository.delete(student);

        return "Student deleted" + studentId;
    }

    // fetch courses using student id

    @Override
    public List<CourseDTO> getCourseByStudentId(int studentId) {

        Student student = studentRepository.findById(studentId).
                orElseThrow(()->
                        new StudentNotFoundException("Student not found : " + studentId));

        List<Course> courseList = student.getCourses();

        if(courseList == null || courseList.isEmpty())
        {
            throw new CourseNotFoundException("Course list is empty for student : " + studentId);
        }

        List<CourseDTO> courseDTOS = new ArrayList<>();

        for(Course course : courseList)
        {
            CourseDTO dto = new CourseDTO();
            dto.setId(course.getCourseId());
            dto.setTitle(course.getTitle());
            courseDTOS.add(dto);
        }

        return courseDTOS;
    }


    @Override
    public Student patchAndSaveStudent(Student student) {

        studentRepository.findById(student.getStudentId()).
                orElseThrow(() -> new StudentNotFoundException
                        ("Student not available : " + student.getStudentId()));

        studentRepository.save(student);

        return student;
    }

    @Override
    public Student fetchStudent(int id)
    {
        return studentRepository.findById(id).
                orElseThrow(()-> new StudentNotFoundException("Student not available : " + id));
    }
}
