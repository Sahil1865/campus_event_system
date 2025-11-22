package com.sahil.campus_event_system.controller;
import com.sahil.campus_event_system.model.Registration;
import com.sahil.campus_event_system.model.User;
import com.sahil.campus_event_system.repository.RegistrationRepository;
import com.sahil.campus_event_system.repository.UserRepository;
import com.sahil.campus_event_system.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public List<Registration> getAllRegistration() {
        return registrationService.getAllRegistration();
    }

    @PostMapping("/{eventId}")
    public String createRegistration(@PathVariable Long eventId, @RequestParam Long userId) {
        return registrationService.createRegistration(userId, eventId);
    }

    @GetMapping("/{id:[0-9]+}")
    public Registration getRegistration(@PathVariable Long id) {
        return registrationService.getRegistration(id);
    }

    @DeleteMapping("/{registrationId}")
    public ResponseEntity<?> deleteRegistration(@PathVariable Long registrationId) {
        registrationService.deleteRegistration(registrationId);
        return ResponseEntity.ok("Registration cancelled successfully");
    }

    @GetMapping("/user/{userId}")
    public List<Registration> getUserRegistrations(@PathVariable Long userId) {
        return registrationService.getRegistrationsByUser(userId);
    }

}


