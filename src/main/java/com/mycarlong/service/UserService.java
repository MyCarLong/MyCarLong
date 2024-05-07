package com.mycarlong.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycarlong.mycarlongback.entity.UserEntity;
import com.mycarlong.mycarlongback.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity registerUser(String name, String email, String password, String contact) {
        // 새로운 사용자 생성
        UserEntity user = new UserEntity();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setContact(contact);
        user.setRole("MEMBER");

        // 데이터베이스에 사용자 정보 저장
        return userRepository.save(user);
    }

    // 다른 사용자 관련 기능들도 추가할 수 있음
}