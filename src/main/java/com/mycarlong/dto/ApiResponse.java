package com.mycarlong.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
  public class ApiResponse {
    
    private boolean success;
    private String message;
    private String name;

    public ApiResponse(boolean success, String message) {
      this.success = success;
      this.message = message;
    }
    
}
