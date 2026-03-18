package com.truckplatform.auth.dto;

import com.truckplatform.users.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    
    private String token;
    private Long userId;
    private String email;
    private String name;
    private UserRole role;
    private String tokenType = "Bearer";
}
