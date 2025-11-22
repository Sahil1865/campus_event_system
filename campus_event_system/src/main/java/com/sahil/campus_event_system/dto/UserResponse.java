package com.sahil.campus_event_system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private String role;
    private String phoneNumber;
}
