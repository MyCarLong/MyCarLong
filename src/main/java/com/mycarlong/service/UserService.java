package com.mycarlong.service;

import com.mycarlong.config.JWTUtil;
import com.mycarlong.entity.QUserEntity;
import com.mycarlong.entity.UserEntity;
import com.mycarlong.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import java.util.List;

@Service
public class UserService{


    @Autowired
    private UserRepository userRepository;

    private final JPAQueryFactory queryFactory;

    @Autowired
    public UserService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${jwt.expiration}")
    private Long expirationTime; // 토큰의 만료 시간을 담을 변수

    @Autowired
    private JWTUtil jwtUtil; // JWTUtil 클래스의 Autowired를 통해 주입

    public String findByPassword(String password, String name) {
        QUserEntity user = QUserEntity.userEntity;

        // 모든 사용자를 가져옴
        List<UserEntity> users = queryFactory.selectFrom(user).fetch();

        // 가져온 데이터를 바탕으로 이름과 비밀번호가 일치하는지 비교
        for (UserEntity foundUser : users) {
            if (foundUser.getName().equals(name) && passwordEncoder.matches(password, foundUser.getPassword())) {
	            logger.info("DSL로 찾은 유저 비밀번호: {}", foundUser.getPassword());
	            logger.info("DSL로 찾은 유저명: {}", foundUser.getName());
	            logger.info("입력한 비밀번호: {}", password);
	            logger.info("비밀번호 일치: {}", passwordEncoder.matches(password, foundUser.getPassword()));
                return password;
            }
        }

        throw new UsernameNotFoundException("존재하지 않는 사용자명 또는 비밀번호입니다.");
    }

   /* public String findByNamePasswordEmailContact(String email, String name,String password, String contact, String newPassword) {
        QUserEntity user = QUserEntity.userEntity;

        // 모든 사용자를 가져옴
        List<UserEntity> users = queryFactory.selectFrom(user).fetch();
        logger.info(users);
        String changePassword = null;
        //이름,비밀번호,전화번호,이메일의 일치여부 확인
        for (UserEntity foundUser : users) {
            if (foundUser.getName().equals(name) &&
                    passwordEncoder.matches(password, foundUser.getPassword()) &&
                    foundUser.getEmail().equals(email) &&
                    foundUser.getContact().equals(contact)) {
                changePassword = newPassword;
                foundUser.setPassword(changePassword);
                logger.info(foundUser.getPassword());
            }
            return foundUser.getPassword();
        }
        throw new UsernameNotFoundException("존재하지 않는 사용자명 또는 비밀번호입니다.");
    }
*/
    public String registerUser(String name, String email, String password, String contact) {
    // 새로운 사용자 생성
    UserEntity user = new UserEntity();
    //이미 가입된 회원일 경우
    if (userRepository.existsByEmail(email)) {
        logger.info("중복된 이메일입니다.");
        throw new UsernameNotFoundException("중복된 이메일입니다.");
    } else {
        String role = "ROLE_USER";
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setContact(contact);
        user.setRole(role);

        // 데이터베이스에 사용자 정보 저장
        user = userRepository.save(user);

        // 사용자 정보를 기반으로 JWT 토큰 생성
	    logger.info("userService username :{}", user.getName());
        String token = jwtUtil.createJwt(user.getEmail(), user.getRole(), expirationTime); // 여기에 만료 시간을 넣어주세요.
	    logger.info("jwt token = {}", token);
        return token;
    }
}

    public boolean verifyAndUpdatePassword(String name, String password, String email, String contact, String newPassword) {
        QUserEntity user = QUserEntity.userEntity;
        logger.info("QUserEntity = {}", user.toString());

        // 모든 사용자를 가져옴
        List<UserEntity> users = queryFactory.selectFrom(user).fetch();
        logger.info("Fetched users = {}", users);

        // 가져온 데이터를 바탕으로 이름과 비밀번호가 일치하는지 비교
        for (UserEntity foundUser : users) {
            logger.info("foundUser = {}", foundUser.toString());
            if (foundUser.getName().equals(name) &&
                    passwordEncoder.matches(password, foundUser.getPassword()) &&
                    foundUser.getEmail().equals(email) &&
                    foundUser.getContact().equals(contact)) {
                foundUser.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(foundUser);
                return true;
            }
        }
        logger.error("사용자 정보를 찾을 수 없습니다.");
        return false;
    }
    // 다른 사용자 관련 기능들도 추가할 수 있음
}