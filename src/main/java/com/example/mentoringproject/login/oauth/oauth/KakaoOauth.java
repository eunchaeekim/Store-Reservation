package com.example.mentoringproject.login.oauth.oauth;

import com.example.mentoringproject.common.exception.AppException;
import com.example.mentoringproject.login.oauth.model.KakaoUserInfo;
import com.example.mentoringproject.login.oauth.model.OAuthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOauth implements SocialOauth {

  private final ObjectMapper objectMapper;
  private final HttpServletResponse response;
  private final RestTemplate restTemplate;

  @Value("${spring.OAuth2.kakao.url}")
  private String LOGIN_URL;

  @Value("${spring.OAuth2.kakao.client-id}")
  private String CLIENT_ID;

  @Value("${spring.OAuth2.kakao.callback-url}")
  private String REDIRECT_URI;

  @Value("${spring.OAuth2.kakao.client-secret}")
  private String CLIENT_SECRET;

  @Value("${spring.OAuth2.kakao.scope}")
  private String ACCESS_SCOPE;

  @Value("${spring.OAuth2.kakao.userinfo-url}")
  private String USERINFO_REQUEST_URL;

  @Value("${spring.OAuth2.kakao.token-url}")
  private String TOKEN_REQUEST_URL;

  private final String RESPONSE_TYPE = "code";

  @Override
  public void sendOauthRedirectURL() {

    String redirectUrl = getRedirectUrl();

    try {
      response.sendRedirect(redirectUrl);
      log.info("redirectUrl = {}", redirectUrl);
    } catch (IOException e) {
      throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "sendRedirectFail");
    }
  }

  private String getRedirectUrl() {

    Map<String, String> params = new HashMap<>();
    params.put("scope", ACCESS_SCOPE);
    params.put("response_type", RESPONSE_TYPE);
    params.put("client_id", CLIENT_ID);
    params.put("redirect_uri", REDIRECT_URI);

    return LOGIN_URL + "?" + params.entrySet().stream()
        .map(x -> x.getKey() + "=" + x.getValue())
        .collect(Collectors.joining("&"));
  }

  @Override
  public OAuthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
    OAuthToken oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
    if (oAuthToken.getAccess_token().isEmpty()) {
      throw new AppException(HttpStatus.BAD_REQUEST, "토큰값이 유효하지 않습니다. 중복토큰 혹은 토큰값 오류");
    }
    return oAuthToken;
  }

  @Override
  public ResponseEntity<String> requestAccessToken(String code) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-type", "application/x-www-form-urlencoded; charset = utf - 8");

    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(
            TOKEN_REQUEST_URL)
        .queryParam("code", code)
        .queryParam("client_id", CLIENT_ID)
        .queryParam("client_secret", CLIENT_SECRET)
        .queryParam("redirect_uri", REDIRECT_URI)
        .queryParam("grant_type", "authorization_code");

    log.info("code ={}", code);
    log.info("CLIENT_ID ={}", CLIENT_ID);
    log.info("TOKEN_REQUEST_URL ={}", TOKEN_REQUEST_URL);
    log.info("CLIENT_SECRET ={}", CLIENT_SECRET);
    log.info("CALLBACK_URL ={}", REDIRECT_URI);

    return restTemplate.postForEntity(uriBuilder.toUriString(),
        new HttpEntity(headers), String.class);
  }


  @Override
  public ResponseEntity<String> requestUserInfo(OAuthToken oAuthToken) {
    return requestUserInfo(oAuthToken, USERINFO_REQUEST_URL);
  }

  public ResponseEntity<String> requestUserInfo(OAuthToken oAuthToken,
      String USERINFO_REQUEST_URL) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());

    ResponseEntity<String> response = restTemplate.exchange(USERINFO_REQUEST_URL,
        HttpMethod.GET, new HttpEntity(headers), String.class);

    return response;
  }

  @Override
  public String getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException {
    KakaoUserInfo user = objectMapper.readValue(userInfoRes.getBody(), KakaoUserInfo.class);
    return user.getId();
  }
}
