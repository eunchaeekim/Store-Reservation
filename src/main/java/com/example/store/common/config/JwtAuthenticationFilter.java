package com.example.store.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    /*
    보안 설정 구성:
    웹 애플리케이션의 SecurityConfig나 XML 설정 파일에서 Spring Security의 보안 설정을 정의합니다. 이 설정에서는 인증 및 권한 부여 규칙, 접근 권한이 필요한 URL 패턴, 사용자 세션 관리 방식 등을 설정합니다.

    클라이언트 요청 처리:
    클라이언트가 웹 애플리케이션에 요청을 보냅니다. 이 요청은 Spring의 DispatcherServlet에 의해 처리됩니다.

    보안 필터 체인 적용:
    DispatcherServlet이 요청을 처리하기 전에, Spring Security의 보안 필터 체인이 적용됩니다. 이 필터 체인은 SecurityFilterChain이라는 구성 요소를 기반으로 설정된 보안 필터들이 연속적으로 실행됩니다. 각 필터는 요청을 가로채고 인증, 인가, 로그인 등을 처리합니다.

    인증 및 인가 처리:
    필터 체인에서는 먼저 인증(authentication) 필터가 동작하여 사용자의 인증 상태를 확인합니다. 사용자의 자격 증명을 확인하고 인증이 성공하면, 다음으로 인가(authorization) 필터가 동작하여 사용자의 권한을 확인합니다. 인가가 허용되면 요청이 계속 진행되고, 인가가 거부되면 에러 페이지 또는 권한 거부 처리를 수행합니다.

    사용자 정보 로드 및 인증 객체 생성:
    인증이 성공적으로 이루어지면, Spring Security는 사용자 정보를 로드하고, 이를 기반으로 Authentication 객체를 생성합니다. 이 객체는 사용자의 인증 상태와 권한 정보를 담고 있습니다.

    보안 컨텍스트 설정:
    생성된 Authentication 객체는 SecurityContext에 저장됩니다. SecurityContext는 현재 스레드의 로컬 변수에 보안 관련 정보를 저장하고 공유합니다.

    클라이언트 요청 처리 계속:
    보안 필터 체인의 모든 처리가 완료되면, DispatcherServlet은 클라이언트의 요청을 일반적인 방식으로 처리합니다. 이 때, SecurityContext에 저장된 인증 정보를 사용하여 사용자 인증 상태를 유지합니다.

    응답 처리:
    요청 처리가 완료되고 응답이 생성될 때, Spring Security는 로그아웃 처리, 보안 헤더 추가, CSRF 방어 등의 추가적인 작업을 수행합니다.

    클라이언트 응답 전달:
    최종적으로 생성된 응답이 클라이언트로 전송됩니다.
     */

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 JWT 를 받아옵니다.
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        // 유효한 토큰인지 확인합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 이 필터가 속한 필터 체인의 다음 필터를 호출하는 데 사용되는 객체
        chain.doFilter(request, response);
    }
}