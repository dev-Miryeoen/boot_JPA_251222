package com.example.demo.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

    // redirect 데이터를 가지고 있는 객체 (redirect 경로 저장(이동) 역할)
    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage; // error 메시지 저장 변수
        String loginFailedEmail = request.getParameter("email");

        if(exception instanceof BadCredentialsException){
            // 아이디 + 패스워드 오류
            errorMessage = "email/password가 일치하지 않습니다.";
        }else {
            errorMessage = "관리자에게 문의해주세요.";
        }

        log.info(">>> loginFailedEmaill >> {}", loginFailedEmail);
        log.info(">>> errorMessage >> {}", errorMessage);

        String url = "/user/login";
        request.getSession().setAttribute("email", loginFailedEmail);
        request.getSession().setAttribute("errMsg", errorMessage);

        redirectStrategy.sendRedirect(request, response, url);
    }
}
