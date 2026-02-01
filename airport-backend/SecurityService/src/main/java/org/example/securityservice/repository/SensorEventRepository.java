package org.example.securityservice.repository;

import org.example.securityservice.model.entity.SensorEvent;
import org.example.securityservice.model.enumeration.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorEventRepository extends JpaRepository<SensorEvent, Long> {

    // Znajdź eventy po ID sensora
    List<SensorEvent> findBySensorId(String sensorId);

    // Znajdź eventy po typie sensora
    List<SensorEvent> findBySensorType(SensorType sensorType);

    // Znajdź eventy z danego zakresu czasowego
    List<SensorEvent> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    // Znajdź eventy związane z incydentem
    List<SensorEvent> findByIncidentId(Long incidentId);

    // Znajdź nieprzypisane eventy (bez incydentu)
    List<SensorEvent> findByIncidentIsNull();

    // Znajdź eventy z danego obszaru/lokalizacji
    List<SensorEvent> findByLocationId(Long locationId);

    // Znajdź krytyczne eventy (powyżej progu)
    @Query("SELECT se FROM SensorEvent se WHERE se.severity >= :threshold")
    List<SensorEvent> findCriticalEvents(@Param("threshold") Double threshold);

    // Liczba eventów w danym okresie
    @Query("SELECT COUNT(se) FROM SensorEvent se WHERE se.timestamp BETWEEN :start AND :end")
    Long countEventsInPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}