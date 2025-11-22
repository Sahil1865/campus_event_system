package com.sahil.campus_event_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class EventRequest {

    private String eventTitle;
    private String eventDescription;
    private float eventPrice;
    private String startTime;
    private String endTime;
    private String eventLocation;
    private Long eventAttendees;
    private Long createdById;
    private List<Long> categoryIds;
}



