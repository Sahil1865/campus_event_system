package com.sahil.campus_event_system.repository;

import com.sahil.campus_event_system.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByCategories_CategoryName(String categoryName);
}
