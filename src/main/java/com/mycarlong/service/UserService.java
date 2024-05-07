package com.mycarlong.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycarlong.mycarlongback.dto.UserDTO;
import com.mycarlong.mycarlongback.entity.UserEntity;
import com.mycarlong.mycarlongback.repository.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    UserDTO userDTO = new UserDTO();

    public UserEntity saveUser(UserEntity user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userDTO.getName());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setContact(userDTO.getContact());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setRole("ROLE_USER");
        return userRepository.save(user);
    }

    // 추가로 필요한 메소드가 있다면 여기에 추가할 수 있습니다.
}
