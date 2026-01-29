package org.example.groundopsservice.repository;

import org.example.groundopsservice.model.entity.TechnicalResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicalResourceRepository extends JpaRepository<TechnicalResource, Long> {}