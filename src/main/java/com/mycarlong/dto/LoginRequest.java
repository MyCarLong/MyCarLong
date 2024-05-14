package com.mycarlong.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
  private String name;
  private String email;
  private String password;
  private String contact;
}
