package com.mycarlong.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
  @NotBlank(message = "이메일을 입력하세요.")
  @Email(message = "유효한 이메일 주소를 입력하세요.")
  private String email;

  @NotBlank(message = "비밀번호를 입력하세요.")
  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 최소 8자 이상의 영문자와 숫자의 조합이어야 합니다.")
  private String password;

  @NotBlank(message = "비밀번호를 다시 입력하세요.")
  private String confirmPassword;

  @NotBlank(message = "이름을 입력하세요.")
  private String name;

  @NotBlank(message = "연락처를 입력하세요.")
  @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "유효한 연락처 형식을 입력하세요. (000-0000-0000)")
  private String contact;

  @NotBlank(message = "비밀번호를 입력하세요.")
  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 최소 8자 이상의 영문자와 숫자의 조합이어야 합니다.")
  private String changePassword;


  // 유효성 검사 메소드
  public boolean isValid() {
    return confirmPassword.equals(password); // 비밀번호 확인 필드가 일치하는지 확인
  }

}