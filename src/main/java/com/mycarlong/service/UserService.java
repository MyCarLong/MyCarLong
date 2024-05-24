package com.mycarlong.service;

import com.mycarlong.config.JWTUtil;
import com.mycarlong.entity.UserEntity;
import com.mycarlong.entity.UserRole;
import com.mycarlong.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private final JPAQueryFactory queryFactory;

    @Autowired
    public UserService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${jwt.expiration}")
    private Long expirationTime; // 토큰의 만료 시간을 담을 변수

    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        UserDetails userDetails = User.builder()
                .username(user.getName())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return userDetails;
    }


    public String findByPassword(String password, String name) {
        UserEntity foundUser = userRepository.findByName(name);

        if (foundUser != null && passwordEncoder.matches(password, foundUser.getPassword())) {
            logger.info("JPA로 찾은 유저 비밀번호: {}", foundUser.getPassword());
            logger.info("JPA로 찾은 유저명: {}", foundUser.getName());
            logger.info("입력한 비밀번호: {}", password);
            logger.info("비밀번호 일치: {}", passwordEncoder.matches(password, foundUser.getPassword()));
            return password;
        } else {
            throw new UsernameNotFoundException("존재하지 않는 사용자명 또는 비밀번호입니다.");
        }
    }


    public UserDetails authenticateAndGenerateToken(String email, String password) {
        UserDetails user = loadUserByUsername(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return user;
        } else {
            throw new UsernameNotFoundException("이메일이나 비밀번호가 올바르지 않습니다.");
        }
    }

    public String registerUser(String name, String email, String password, String contact) {
        if (userRepository.existsByEmail(email)) {
            logger.info("중복된 이메일입니다.");
            throw new UsernameNotFoundException("중복된 이메일입니다.");
        } else {
            UserEntity user = new UserEntity();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setContact(contact);
            user.setRole(UserRole.USER);

            user = userRepository.save(user);

            logger.info("userService username :{}", user.getName());
            return generateToken(user.getEmail(), user.getRole().toString());
        }
    }

    public boolean verifyAndUpdatePassword(String name, String password, String email, String contact, String newPassword) {
        logger.info("입력된 정보 - 이름: {}, 이메일: {}, 연락처: {}", name, email, contact);

        Optional<UserEntity> optionalUser = userRepository.findByNameAndEmailAndContact(name, email, contact);

        if (optionalUser.isPresent()) {
            UserEntity foundUser = optionalUser.get();
            logger.info("조회된 사용자 정보: {}", foundUser);

            if (passwordEncoder.matches(password, foundUser.getPassword())) {
                foundUser.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(foundUser);
                logger.info("비밀번호가 성공적으로 업데이트되었습니다.");
                return true;
            } else {
                logger.error("비밀번호가 일치하지 않습니다.");
            }
        } else {
            logger.error("사용자 정보를 찾을 수 없습니다.");
        }
        return false;
    }

    public String generateToken(String username, String role) {
        return jwtUtil.createJwt(username, role, expirationTime);
    }
}