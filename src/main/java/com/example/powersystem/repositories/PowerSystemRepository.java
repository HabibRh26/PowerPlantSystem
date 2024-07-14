package com.example.powersystem.repositories;

import com.example.powersystem.Entities.Battery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerSystemRepository extends JpaRepository<Battery, Long> {
}
