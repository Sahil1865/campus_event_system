package com.sahil.campus_event_system.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter @Setter
public class EventResponse {
    private Long eventId;
    private String eventTitle;
    private String eventDescription;
    private float eventPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String eventLocation;
    private Long eventAttendees;
    private String eventImageUrl;
    private LocalDateTime eventCreatedAt;

    private UserResponse createdBy;
    private Set<CategoryResponse> categories;
    private String createdByName;
    private List<String> categoryNames;
}
