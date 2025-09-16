package com.example.movie_app.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {

    EMAIL_EXIST(HttpStatus.CONFLICT.value(), "Email already registered"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "Invalid token"),
    CANNOT_DELETE_ADMIN(HttpStatus.FORBIDDEN.value(), "Cannot delete administrator"),
    INVALID_USER(HttpStatus.BAD_REQUEST.value(),  "Invalid username or password"),
    MOVIE_EXIST(HttpStatus.CONFLICT.value(), "Movie already registered"),;
    private final int statusCode;
    private final String message;

    ErrorCode(int statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }
}
