package com.sahil.campus_event_system.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class RegistrationResponse {
    private Long registrationId;
    private String status;
    private LocalDateTime registeredAt;
    private EventResponse event;
    private UserResponse user;
}
