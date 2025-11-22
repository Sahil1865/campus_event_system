package com.sahil.campus_event_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long eventId;

    @NotBlank(message = "Field cannot be empty")
    private String eventTitle;

    private String eventDescription;

    private float eventPrice;


    private LocalDateTime startTime;


    private LocalDateTime endTime;

    @NotBlank(message = "Field cannot be empty")
    private String eventLocation;

    private Long eventAttendees;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User createdBy;


    @ManyToMany
    @JoinTable(
            name = "event_category",
            joinColumns = @JoinColumn(name = "eventId"),
            inverseJoinColumns = @JoinColumn(name = "categoryId")
    )
    private Set<Category> categories= new HashSet<>();

    private String eventImageUrl;

    private LocalDateTime eventCreatedAt= LocalDateTime.now();
}
