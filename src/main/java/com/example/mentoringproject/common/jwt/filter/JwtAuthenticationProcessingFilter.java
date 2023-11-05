package com.example.mentoringproject.common.jwt.filter;

import com.example.mentoringproject.common.jwt.service.JwtService;
import com.example.mentoringproject.user.entity.User;
import com.example.mentoringproject.user.repository.UserRepository;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 1. RefreshToken이 없고, AccessToken이 없거나 유효하지 않은 경우 -> 인증 실패 처리, 403 ERROR
 * 2. RefreshToken이 없고, AccessToken이 유효한 경우 -> 인증 성공 처리, RefreshToken을 재발급하지는 않는다.
 * 3. RefreshToken이 있는 경우 -> DB의 RefreshToken과 비교하여 일치하면 AccessToken 재발급, RefreshToken 재발급(RTR 방식)
 *                              인증 성공 처리는 하지 않고 실패 처리
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

  private static final String NO_CHECK_URL = "/login"; // "/login"으로 들어오는 요청은 Filter 작동 X
  public static final String AUTHORIZATION_HEADER = "Authorization";

  private final JwtService jwtService;
  private final UserRepository userRepository;

  private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    Optional<String> header = resolveToken(request);
    if (header.isPresent()) {
      if (request.getRequestURI().equals(NO_CHECK_URL)) {
        filterChain.doFilter(request, response);
        return;
      }

      String refreshToken = jwtService.extractRefreshToken(request)
          .filter(jwtService::isTokenValid)
          .orElse(null);
      if (refreshToken == null) {
        checkAccessTokenAndAuthentication(request, response, filterChain);
      } else {
        checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
      }
    } else {
      filterChain.doFilter(request, response);
    }
  }

  private Optional<String> resolveToken(HttpServletRequest request) {

    String authToken = request.getHeader(AUTHORIZATION_HEADER);
    log.debug("authToken: " + authToken);
    if (StringUtils.hasText(authToken)) {
      return Optional.of(authToken);
    } else {
      return Optional.empty();
    }
  }

  public void checkAccessTokenAndAuthentication(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("checkAccessTokenAndAuthentication() 호출");

    jwtService.extractAccessToken(request)
        .filter(jwtService::isTokenValid)
        .flatMap(accessToken -> jwtService.extractEmail(accessToken))
        .filter(Objects::nonNull)
        .flatMap(email -> userRepository.findByEmail(email))
        .filter(Objects::nonNull)
        .ifPresent(this::saveAuthentication);

    filterChain.doFilter(request, response);
  }

  public void saveAuthentication(User myUser) {
    String password = myUser.getPassword();
    if (password == null) {
      password = UUID.randomUUID().toString();
    }

    UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
        .username(myUser.getEmail())
        .password(password)
        .authorities("USER")
        .build();

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(userDetailsUser, null,
            authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response,
      String refreshToken) {
    userRepository.findByRefreshToken(refreshToken)
        .ifPresent(user -> {
          String reIssuedRefreshToken = reIssueRefreshToken(user);
          jwtService.sendAccessAndRefreshToken(response,
              jwtService.createAccessToken(user.getEmail()),
              reIssuedRefreshToken);
        });
  }

  private String reIssueRefreshToken(User user) {
    String reIssuedRefreshToken = jwtService.createRefreshToken();
    user.updateRefreshToken(reIssuedRefreshToken);
    userRepository.saveAndFlush(user);
    return reIssuedRefreshToken;
  }
}
