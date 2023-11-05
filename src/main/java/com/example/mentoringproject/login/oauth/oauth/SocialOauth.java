package com.example.mentoringproject.login.oauth.oauth;


import com.example.mentoringproject.login.oauth.model.OAuthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import org.springframework.http.ResponseEntity;


public interface SocialOauth {

  void sendOauthRedirectURL() throws IOException;

  OAuthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException;

  ResponseEntity<String> requestAccessToken(String code);

  ResponseEntity<String> requestUserInfo(OAuthToken oAuthToken);

  String getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException;
}
