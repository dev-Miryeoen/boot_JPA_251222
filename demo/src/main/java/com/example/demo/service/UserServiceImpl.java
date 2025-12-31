package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.AuthRole;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public String register(UserDTO userDTO) {
        userDTO.setPwd(passwordEncoder.encode(userDTO.getPwd()));
        User user = convertDtoToEntity(userDTO);
        user.addAuth(AuthRole.USER);
        // cascade 설정으로 인해 AuthUser 테이블도 자동 저장됨.
        return userRepository.save(user).getEmail();
    }

    @Transactional
    @Override
    public void lastLoginUpdate(String name) {
        User user = userRepository.findById(name).orElseThrow(() -> new EntityNotFoundException("해당 사용자가 없습니다."));
        // 상태변경 (dirty checking 발생) => transactional
        user.setLastLogin(LocalDateTime.now());
    }

    @Transactional
    @Override
    public String modify(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getEmail())
                .orElseThrow(()-> new EntityNotFoundException("해당 유저가 없습니다."));
        if(!userDTO.getPwd().isEmpty() && userDTO.getPwd() != null){
            String changePwd = passwordEncoder.encode(userDTO.getPwd());
            user.setPwd(changePwd);
        }
        user.setNickName(userDTO.getNickName());
        return user.getEmail();
    }

    @Override
    public List<UserDTO> getList() {
        List<User> userList = userRepository.findAllWithAuthList();
        return userList.stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    public UserDTO getDetail(String name){
        User user = userRepository.findByEmailWithAuth(name)
                .orElseThrow(()-> new EntityNotFoundException("해당 유저가 없습니다.."));
        return convertEntityToDto(user);
    }

    @Transactional
    public String remove(String email){
        User user= userRepository.findById(email)
                .orElseThrow(()-> new EntityNotFoundException("해당 유자가 없습니다."));
        userRepository.delete(user);
        return user.getEmail();
    }
}
