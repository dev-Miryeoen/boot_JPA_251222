package com.example.demo.service;

import com.example.demo.dto.AuthUserDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.AuthRole;
import com.example.demo.entity.AuthUser;
import com.example.demo.entity.User;

public interface UserService {

    // convert DtoToEntity
    default User convertDtoToEntity(UserDTO userDTO){
        return User.builder()
                .email(userDTO.getEmail())
                .pwd(userDTO.getPwd())
                .nickName(userDTO.getNickName())
                .lastLogin(userDTO.getLastLogin())
                .build();
    }

    // Entity => Dto
    default UserDTO convertEntityToDto(User user){
        return UserDTO.builder()
                .email(user.getEmail())
                .nickName(user.getNickName())
                .lastLogin(user.getLastLogin())
                .regDate(user.getRegDate())
                .modDate(user.getModDate())
                .authList(user.getAuthList() == null? null :
                        user.getAuthList().stream()
                                .map(this::convertAuthEntityToAuthDto)
                                .toList())
                .build();
    }

    // authDto 변환
    default AuthUserDTO convertAuthEntityToAuthDto(AuthUser authUser){
        return AuthUserDTO.builder()
                .id(authUser.getId())
                .role(authUser.getAuth().name())
                .email(authUser.getUser().getEmail())
                .build();
    }

    // authEntity 변환
    default AuthUser convertAuthDtoToEntity(AuthUserDTO authUserDTO){
        return AuthUser.builder()
                .id(authUserDTO.getId())
                .auth(AuthRole.USER)
                .build();
    }

    String register(UserDTO userDTO);
}
