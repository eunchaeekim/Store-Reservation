package com.example.mentoringproject.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class AppException extends RuntimeException{
    private HttpStatus errorCode;
    private String message;
}
