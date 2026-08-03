package com.example.demo.repository;

import com.example.demo.entity.ProjectionEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectionRepository extends JpaRepository<ProjectionEntity, UUID> {}
