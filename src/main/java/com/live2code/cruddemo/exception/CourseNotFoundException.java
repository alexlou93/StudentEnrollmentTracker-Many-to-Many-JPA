package com.live2code.cruddemo.exception;

public class CourseNotFoundException extends RuntimeException{

    public  CourseNotFoundException(String message){
        super(message);
    }
}
