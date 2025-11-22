package com.sahil.campus_event_system.controller;

import com.sahil.campus_event_system.dto.EventRequest;
import com.sahil.campus_event_system.dto.EventResponse;
import com.sahil.campus_event_system.service.EventService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventApiController {

    private final EventService eventService;

    public EventApiController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEvent(
            @RequestPart("event") EventRequest request,
            @RequestPart("imageFile") MultipartFile file
    ) throws IOException {

        EventResponse saved = eventService.createEvent(request, file);
        return ResponseEntity.ok(Map.of(
                "message", "Event created successfully",
                "event", saved
        ));
    }

    @GetMapping
    public List<EventResponse> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventResponse getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}
