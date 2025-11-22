package com.sahil.campus_event_system.repository;

import com.sahil.campus_event_system.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration,Long> {
    boolean existsByUser_UserIdAndEvent_EventId(Long userId, Long eventId);
    List<Registration> findByUser_UserId(Long userId);

}
