package com.sahil.campus_event_system.service;

import com.sahil.campus_event_system.dto.CategoryResponse;
import com.sahil.campus_event_system.dto.EventRequest;
import com.sahil.campus_event_system.dto.EventResponse;
import com.sahil.campus_event_system.dto.UserResponse;
import com.sahil.campus_event_system.model.Category;
import com.sahil.campus_event_system.model.Event;
import com.sahil.campus_event_system.model.User;
import com.sahil.campus_event_system.repository.CategoryRepository;
import com.sahil.campus_event_system.repository.EventRepository;
import com.sahil.campus_event_system.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public EventService(EventRepository eventRepository,
                        UserRepository userRepository,
                        CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }


    // ------------------- GET ALL EVENTS ------------------
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // ------------------- GET EVENT BY ID ------------------
    public EventResponse getEventById(Long id) {
        return eventRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }


    // ------------------- CREATE EVENT ------------------
    public EventResponse createEvent(EventRequest request, MultipartFile imageFile) throws IOException {

        // UPLOAD LOGIC
        String uploadDir = "uploads/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // -------------------------------
        //  FIND CREATOR (IMPORTANT FIX)
        // -------------------------------
        User createdBy = userRepository.findById(request.getCreatedById())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getCreatedById()));

        // SAVE EVENT
        Event e = new Event();
        e.setEventTitle(request.getEventTitle());
        e.setEventDescription(request.getEventDescription());
        e.setEventLocation(request.getEventLocation());
        e.setEventAttendees(request.getEventAttendees());
        e.setEventPrice(request.getEventPrice());
        e.setStartTime(LocalDateTime.parse(request.getStartTime()));
        e.setEndTime(LocalDateTime.parse(request.getEndTime()));
        e.setEventImageUrl("/uploads/" + fileName);
        e.setCreatedBy(createdBy);

        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());
            e.setCategories(new HashSet<>(categories));
        }


        Event saved = eventRepository.save(e);

        return mapToResponse(saved);
    }


    // ------------------- DELETE EVENT ------------------
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new IllegalArgumentException("Event not found");
        }
        eventRepository.deleteById(id);
    }


    // ------------------- GET BY CATEGORY ------------------
    public List<EventResponse> getEventsByCategoryName(String categoryName) {
        List<Event> events = eventRepository.findByCategories_CategoryName(categoryName);
        return events.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // ------------------- MAP EVENT TO DTO ------------------
    private EventResponse mapToResponse(Event e) {
        EventResponse r = new EventResponse();

        r.setEventId(e.getEventId());
        r.setEventTitle(e.getEventTitle());
        r.setEventDescription(e.getEventDescription());
        r.setEventLocation(e.getEventLocation());
        r.setEventImageUrl(e.getEventImageUrl());
        r.setStartTime(e.getStartTime());
        r.setEndTime(e.getEndTime());
        r.setEventAttendees(e.getEventAttendees());
        r.setEventPrice(e.getEventPrice());
        r.setEventCreatedAt(e.getEventCreatedAt());
        r.setCreatedByName(e.getCreatedBy().getUserName());
        r.setCategoryNames(
                e.getCategories()
                        .stream()
                        .map(Category::getCategoryName)
                        .toList()
        );



        // ---- CreatedBy Mapping ----
        if (e.getCreatedBy() != null) {
            UserResponse u = new UserResponse();
            u.setUserId(e.getCreatedBy().getUserId());
            u.setName(e.getCreatedBy().getUserName());
            u.setEmail(e.getCreatedBy().getEmail());
            u.setPhoneNumber(e.getCreatedBy().getUserContact());
            u.setRole(e.getCreatedBy().getUserRole());
            r.setCreatedBy(u);
        }


        // ---- Category Mapping ----
        Set<CategoryResponse> categories = new HashSet<>();
        for (Category c : e.getCategories()) {
            CategoryResponse cr = new CategoryResponse();
            cr.setCategoryId(c.getCategoryId());
            cr.setCategoryName(c.getCategoryName());
            categories.add(cr);
        }
        r.setCategories(categories);

        return r;
    }
}
