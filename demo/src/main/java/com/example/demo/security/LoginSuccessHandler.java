package com.example.demo.security;

import com.example.demo.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    // redirect 데이터를 가지고 있는 객체 (redirect 경로 저장(이동) 역할)
    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    // 세션정보, 직전 url 정보를 가지고 있는 객체
    RequestCache requestCache = new HttpSessionRequestCache();

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // lastLogin 기록
        // 현재 로그인 객체를 가져와서 => lastLogin set
        // authentication.getName() => username
        userService.lastLoginUpdate(authentication.getName());

        HttpSession ses = request.getSession();
        if(ses == null){
            return;
        }else{
            // 이전 로그인 기록 지우기
            ses.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

        // 이전 mapping 경로 가져오기 => 없으면 /board/list로 보내기
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        redirectStrategy.sendRedirect(request, response,
                savedRequest != null ? savedRequest.getRedirectUrl() : "/board/list");

    }
}
