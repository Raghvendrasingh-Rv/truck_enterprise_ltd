package com.truckplatform.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String role;

    private LocalDateTime createdAt;
}
