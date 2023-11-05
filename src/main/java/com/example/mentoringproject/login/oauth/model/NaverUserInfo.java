package com.example.mentoringproject.login.oauth.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverUserInfo {

  private String resultcode;
  private String message;
  private Response response;

}


