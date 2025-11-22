package com.sahil.campus_event_system.repository;

import com.sahil.campus_event_system.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
