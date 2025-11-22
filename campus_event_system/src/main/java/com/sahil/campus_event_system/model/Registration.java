package com.sahil.campus_event_system.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "registration",
        uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "eventId"}))
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long registrationId;

    @NotNull(message = "Event is required")
    @ManyToOne
    @JoinColumn(name = "eventId")
    private Event event;

    @NotNull(message = "User is required")
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String status;

    private LocalDateTime registeredAt = LocalDateTime.now();
}
