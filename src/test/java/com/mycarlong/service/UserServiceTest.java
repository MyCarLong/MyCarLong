package com.mycarlong.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.mycarlong.config.JWTUtil;
import com.mycarlong.entity.UserEntity;
import com.mycarlong.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        UserEntity user = new UserEntity();
        user.setName("testUser");
        user.setEmail("testEmail");
        user.setPassword(passwordEncoder.encode("testPassword"));
        user.setContact("testContact");
        user.setRole("ROLE_USER");

        long expirationTime = 1000L; // 예시 만료 시간 설정

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        when(jwtUtil.createJwt(anyString(), anyString(), anyLong())).thenReturn("mockToken");

        String expectedToken = "mockToken";
        String result = userService.registerUser(user.getName(), user.getEmail(), "testPassword", user.getContact());
        assertEquals(expectedToken, result);

        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userRepository, times(1)).save(any(UserEntity.class));

        // ArgumentCaptor를 사용하여 실제로 저장된 UserEntity 확인
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());
        UserEntity capturedUser = userCaptor.getValue();
        assertEquals("testUser", capturedUser.getName());
        assertEquals("testEmail", capturedUser.getEmail());
        assertEquals(passwordEncoder.encode("testPassword"), capturedUser.getPassword()); // 비밀번호 인코딩 확인
        assertEquals("testContact", capturedUser.getContact());
        assertEquals("ROLE_USER", capturedUser.getRole());

        // 콘솔 로그를 확인하기 위해 System.out.print 메시지 추가
        System.out.println("userService username :" + capturedUser.getName());
        System.out.println("jwt token = " + result);
    }
}

