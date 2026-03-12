package com.live2code.cruddemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CourseRequestDTO {

    @NotBlank(message = "Course title cannot be empty")
    @Size(min = 3, max = 100, message = "Course title must be between 3 and 100 characters")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
