package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.http.HttpRequest;
import java.security.Principal;
import java.util.List;

@Slf4j
@RequestMapping("/user/*")
@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @GetMapping("/join")
    public void join(){

    }

//    @GetMapping("/login")
//    public void login(){}

    @GetMapping("/login")
    public void login(HttpServletRequest request,
                      Model model){
        String email = (String)request.getSession().getAttribute("email");
        String errMsg = (String)request.getSession().getAttribute("errMsg");
        if(errMsg != null){
            log.info(">>> errMsg >> {}", errMsg);
            model.addAttribute("email", email);
            model.addAttribute("errMsg", errMsg);

        }
        request.getSession().removeAttribute("email");
        request.getSession().removeAttribute("errMsg");
    }

    @PostMapping("/join")
    public String join(UserDTO userDTO){
        String email = userService.register(userDTO);
        log.info(">>> email >> {}", email);
        return "index";
    }

//    @GetMapping("/logout")
//    public String logout(){
//
//        return "index";
//    }

    @GetMapping("/modify")
    public void modify(Model model, Principal principal){
        UserDTO userDTO = userService.getDetail(principal.getName());
        log.info(">>> userDTO >> {}", userDTO);
        model.addAttribute("userDTO", userDTO);
    }

    @PostMapping("/modify")
    public String modify(UserDTO userDTO,
                         HttpServletRequest request,
                         HttpServletResponse response,
                         RedirectAttributes redirectAttributes){

        String email = userService.modify(userDTO);
        if(email != null){
            redirectAttributes.addFlashAttribute("modMsg", "ok");
        }else{
            redirectAttributes.addFlashAttribute("modMsg", "fail");
        }
        // 하단 로그아웃 메서드 호출
        logout(request, response);
        return "redirect:/";
    }

    @GetMapping("/list")
    public void list(Model model){
        List<UserDTO> userList = userService.getList();
        model.addAttribute("userList", userList);
    }

    private void logout(HttpServletRequest request,
                        HttpServletResponse response){
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler()
                    .logout(request, response, auth);
        }
    }


    @GetMapping("/remove")
    public String remove(RedirectAttributes redirectAttributes, Principal principal,
                       HttpServletRequest request, HttpServletResponse response){
        String email = principal.getName();
        email = userService.remove(email);
        if(email != null){
            redirectAttributes.addFlashAttribute("delMsg", "ok");
        }else{
            redirectAttributes.addFlashAttribute("delMsg", "fail");
        }
        // 하단 로그아웃 메서드 호출
        logout(request, response);
        return "redirect:/";
    }
}
