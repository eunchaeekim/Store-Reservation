package com.example.mentoringproject.login.oauth.controller;

import com.example.mentoringproject.login.oauth.service.OAuth2Service;
import com.example.mentoringproject.user.entity.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/login")
public class OAuthController {

  private final OAuth2Service oauthService;

  @GetMapping("/oauth/test/{socialLoginType}")
  public void socialLoginRedirect(@PathVariable(name = "socialLoginType") String SocialLoginPath) {
    SocialType socialLoginType = SocialType.valueOf(SocialLoginPath.toUpperCase());
    oauthService.sendRedirectUri(socialLoginType);
  }

  @ResponseBody
  @GetMapping("/oauth/callback/kakao")
  public void loginKakaoCallback(
      @RequestParam(name = "code") String code) throws IOException {
    oauthService.oAuthLogin(SocialType.KAKAO, code);
  }

  @ResponseBody
  @GetMapping(value = "/oauth/callback/naver")
  public void loginCallback(
      @RequestParam(name = "code") String code) throws IOException {
    oauthService.oAuthLogin(SocialType.NAVER, code);
  }
}
