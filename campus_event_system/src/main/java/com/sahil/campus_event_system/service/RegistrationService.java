package com.sahil.campus_event_system.service;
import com.sahil.campus_event_system.model.Event;
import com.sahil.campus_event_system.model.Registration;
import com.sahil.campus_event_system.model.User;
import com.sahil.campus_event_system.repository.EventRepository;
import com.sahil.campus_event_system.repository.RegistrationRepository;
import com.sahil.campus_event_system.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public RegistrationService(RegistrationRepository registrationRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }


    public List<Registration> getAllRegistration() {
        return registrationRepository.findAll();
    }


    public String createRegistration(Long userId, Long eventId) {
        if (registrationRepository.existsByUser_UserIdAndEvent_EventId(userId, eventId)) {
            return "You are already registered for this event!";
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Registration registration = new Registration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setStatus("Registered");
        registrationRepository.save(registration);

        return "You have successfully registered for the event!";
    }


    public Registration getRegistration(@PathVariable Long id) {
        return registrationRepository.findById(id).orElse(null);
    }


    public void deleteRegistration(@PathVariable Long id) {
        registrationRepository.deleteById(id);
    }


    public List<Registration> getRegistrationsByUser(Long userId) {
        return registrationRepository.findByUser_UserId(userId);
    }

}

