package com.mycarlong.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
  public class ApiResponse {
    
    private boolean success;
    private String message;
    private String name;
    private String token;

    public ApiResponse(boolean success, String message) {
      this.success = success;
      this.message = message;
    }

    public ApiResponse(boolean success, String message, String name){
      this.success = success;
      this.message = message;
      this.name = name;
    }
    public ApiResponse(boolean success, String message,String name, String token){
      this.success = success;
      this.message = message;
      this.name = name;
      this.token = token;
    }
    
}
