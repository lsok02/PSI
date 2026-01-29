package org.example.securityservice.repository;

import org.example.securityservice.model.entity.AirportResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportResourceRepository extends JpaRepository<AirportResource, Long> {
}