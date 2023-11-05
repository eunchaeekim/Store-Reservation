package com.example.mentoringproject.common.response;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class ResponseDto {
  private HttpStatus httpStatus;
  private String message;
}
