package com.example.mentoringproject.login.oauth.oauth;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.login.oauth.model.NaverUserInfo;
import com.example.mentoringproject.login.oauth.model.OAuthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class NaverOauth implements SocialOauth {

  private final ObjectMapper objectMapper;
  private final HttpServletResponse response;
  private final RestTemplate restTemplate;

  @Value("${spring.OAuth2.naver.url}")
  private String LOGIN_URL;

  @Value("${spring.OAuth2.naver.client-id}")
  private String CLIENT_ID;

  @Value("${spring.OAuth2.naver.callback-url}")
  private String REDIRECT_URI;

  @Value("${spring.OAuth2.naver.client-secret}")
  private String CLIENT_SECRET;

  @Value("${spring.OAuth2.naver.scope}")
  private String ACCESS_SCOPE;

  @Value("${spring.OAuth2.naver.userinfo-url}")
  private String USERINFO_REQUEST_URL;

  @Value("${spring.OAuth2.naver.token-url}")
  private String TOKEN_REQUEST_URL;

  private final String RESPONSE_TYPE = "code";

  private final String STATE = "1234";

  private final String AUTHORIZATION_CODE = "authorization_code";

  public NaverOauth(ObjectMapper objectMapper, HttpServletResponse response,
      RestTemplate restTemplate) {
    this.objectMapper = objectMapper;
    this.response = response;
    this.restTemplate = restTemplate;
  }

  @Override
  public void sendOauthRedirectURL() {

    //Redirect-url 생성 함수
    String url = getRedirectUrl();

    //Redirect-url 요청 함수
    try {
      response.sendRedirect(url);
      log.info("url = {}", url);
    } catch (IOException e) {
      throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "sendRedirectFail");
    }

  }

  private String getRedirectUrl() {

    Map<String, String> params = new HashMap<>();
    params.put("scope", ACCESS_SCOPE);
    params.put("response_type", RESPONSE_TYPE);
    params.put("client_id", CLIENT_ID);
    params.put("state", STATE);
    params.put("redirect_uri", REDIRECT_URI);

    return LOGIN_URL + "?" + params.entrySet().stream()
        .map(x -> x.getKey() + "=" + x.getValue())
        .collect(Collectors.joining("&"));
  }


  public ResponseEntity<String> requestAccessToken(String code) {

    RestTemplate restTemplate = new RestTemplate();
    Map<String, Object> params = new HashMap<>();

    params.put("code", code);
    params.put("state", STATE);
    params.put("client_id", CLIENT_ID);
    params.put("client_secret", CLIENT_SECRET);
    params.put("grant_type", AUTHORIZATION_CODE);

    String parameterString = params.entrySet().stream()
        .map(x -> x.getKey() + "=" + x.getValue())
        .collect(Collectors.joining("&"));

    String redirectURL = TOKEN_REQUEST_URL + "?" + parameterString;

    return restTemplate.getForEntity(redirectURL, String.class);
  }

  @Override
  public OAuthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
    OAuthToken oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
    if (oAuthToken.getAccess_token()==null) {
      throw new AppException(HttpStatus.BAD_REQUEST, "토큰값이 유효하지 않습니다. 중복토큰 혹은 토큰값 오류");
    }
    return oAuthToken;
  }

  @Override
  public ResponseEntity<String> requestUserInfo(OAuthToken oAuthToken) {
    return requestUserInfo(oAuthToken, USERINFO_REQUEST_URL);
  }

  private ResponseEntity<String> requestUserInfo(OAuthToken oAuthToken, String url) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());

    ResponseEntity<String> response = restTemplate.exchange(url,
        HttpMethod.GET, new HttpEntity(headers), String.class);

    return response;
  }

  @Override
  public String getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException {
    NaverUserInfo naverUserInfo = objectMapper.readValue(userInfoRes.getBody(),
        NaverUserInfo.class);

    return naverUserInfo.getResponse().getId();
  }

}
