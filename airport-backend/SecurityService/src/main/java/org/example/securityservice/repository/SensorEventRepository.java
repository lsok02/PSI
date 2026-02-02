package org.example.securityservice.repository;

import org.example.securityservice.model.entity.SensorEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorEventRepository extends JpaRepository<SensorEvent, Long> {

    List<SensorEvent> findByIncidentIsNull();

    SensorEvent getSensorEventById(Long alarmId);
}