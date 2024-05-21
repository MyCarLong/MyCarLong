package com.mycarlong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.mycarlong.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, QuerydslPredicateExecutor<UserEntity> {

    UserEntity findByEmail(String email);
    boolean existsByEmail(String email);

    UserEntity findByNameAndPasswordAndEmailAndContact(String name, String password, String email, String contact);


}
