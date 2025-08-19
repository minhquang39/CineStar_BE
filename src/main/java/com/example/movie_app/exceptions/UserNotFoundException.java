package com.example.movie_app.exceptions;

public class UserNotFoundException extends  RuntimeException{
    public UserNotFoundException(String message){
        super(message);
    }
}
