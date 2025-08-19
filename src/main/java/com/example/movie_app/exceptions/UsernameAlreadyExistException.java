package com.example.movie_app.exceptions;

public class UsernameAlreadyExistException extends RuntimeException {
    public UsernameAlreadyExistException(String username) {
        super("Username already exists: " + username);
    }
}
