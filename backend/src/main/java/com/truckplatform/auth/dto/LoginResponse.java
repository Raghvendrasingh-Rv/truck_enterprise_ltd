package com.truckplatform.auth.dto;

import com.truckplatform.users.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
