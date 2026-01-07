package com.live2code.cruddemo.entity;

import jakarta.persistence.*;
import org.hibernate.collection.internal.StandardIdentifierBagSemantics;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int courseId;

    @Column(name = "title")
    private String title;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
    mappedBy = "courses")
    private List<Student> students;

    public Course(){

    }

    public Course(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", title='" + title + '\'' +
                '}';
    }

    // add convenience method
    public void addStudent(Student theStudent){
        if(students == null){
            students = new ArrayList<>();
        }
        students.add(theStudent);

        if (theStudent.getCourses() == null) {
            theStudent.setCourses(new ArrayList<>());
        }
        theStudent.getCourses().add(this);
    }

}
