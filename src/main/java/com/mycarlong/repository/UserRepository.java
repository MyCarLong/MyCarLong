package com.mycarlong.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycarlong.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);
}
