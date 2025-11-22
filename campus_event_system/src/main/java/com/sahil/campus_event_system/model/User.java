package com.sahil.campus_event_system.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @NotBlank(message = "What do we call you?")
    private String userName;

    @Email(message = "Invalid email format")
    @Column(name="user_email",unique = true,nullable = false)
    @NotBlank(message = "Email cannot be remained blank")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6,message = "Password should be more than 6 characters")
    @JsonIgnore
    private String userPass;

    private String userRole = "USER";

    @Column(unique = true,length = 10)
    private String userContact;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm a")
    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Event> createdEvents = new HashSet<>();
}
