package com.mycarlong.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycarlong.mycarlongback.entity.UserEntity;
import com.mycarlong.mycarlongback.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public UserEntity registerUser(String name, String email, String password, String contact) {
        // 새로운 사용자 생성
        UserEntity user = new UserEntity();
        if(userRepository.existsByEmail(email)){
            System.out.println("중복된 이메일입니다.");
            return null;
        }else{
            String role = "ROLE_USER";
            user.setName(name);
            user.setEmail(email);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setContact(contact);
            user.setRole(role);
            
            // 데이터베이스에 사용자 정보 저장
            return userRepository.save(user);
        }
    }

    // 다른 사용자 관련 기능들도 추가할 수 있음
}