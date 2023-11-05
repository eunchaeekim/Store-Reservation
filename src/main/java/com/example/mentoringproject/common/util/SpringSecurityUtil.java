package com.example.mentoringproject.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class SpringSecurityUtil {

  public static String getLoginEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getName() == null) {
      throw new RuntimeException("get authentication 오류");
    }
    return authentication.getName();

  }
}
