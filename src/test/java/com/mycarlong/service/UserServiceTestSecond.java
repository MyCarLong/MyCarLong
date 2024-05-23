//package com.mycarlong.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import com.mycarlong.entity.UserEntity;
//import com.mycarlong.repository.UserRepository;
//import com.mycarlong.service.UserService;
//import jakarta.persistence.EntityManager;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import jakarta.persistence.EntityManager;
//@DataJpaTest
//public class UserServiceTestSecond {
//
//	@Autowired
//	private UserRepository userRepository;
//
//	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//	@Test
//	public void testFindByPassword() {
//		// given
//		UserEntity user = new UserEntity();
//		user.setName("TEST");
//		user.setPassword(passwordEncoder.encode("asdasdasd"));
//		userRepository.save(user);
//
//		UserService userService = new UserService(entityManager);
//
//		// when
//		String result = userService.findByPassword("testPassword", "testUser");
//
//		// then
//		assertThat(result).isEqualTo("testPassword");
//
//		// invalid password
//		assertThrows(UsernameNotFoundException.class, () -> {
//			userService.findByPassword("wrongPassword", "testUser");
//		});
//
//		// invalid username
//		assertThrows(UsernameNotFoundException.class, () -> {
//			userService.findByPassword("testPassword", "wrongUser");
//		});
//	}
//}
