package org.example.groundopsservice.repository;

import org.example.groundopsservice.model.entity.BaggageLoading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaggageLoadingRepository extends JpaRepository<BaggageLoading, Long> {}