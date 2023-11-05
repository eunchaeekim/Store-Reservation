package com.example.mentoringproject.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserJoinDto {

  private String email;
  private String password;
  private String nickName;

}