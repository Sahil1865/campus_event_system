package com.sahil.campus_event_system.controller;

import com.sahil.campus_event_system.service.EventService;
import com.sahil.campus_event_system.dto.EventResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EventPageController {

    private final EventService eventService;

    public EventPageController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public String showEvents(Model model) {
        List<EventResponse> events = eventService.getAllEvents();
        model.addAttribute("events", events);
        return "events"; // events.html
    }
}
