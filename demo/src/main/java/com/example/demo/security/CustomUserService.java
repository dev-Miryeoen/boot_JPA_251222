package com.example.demo.security;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class CustomUserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // security content 객체가 username을 주고 해당 객체를
        // DB에서 가져와  UserDetails 객체로 리턴
        User user = userRepository
                .findByEmailWithAuth(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found:"+ username));
        log.info(">>> login user >> {}", user);

        return new CustomAuthUser(user);
    }
}
