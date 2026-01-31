package org.example.securityservice.repository;

import org.example.securityservice.model.entity.Dispatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DispatcherRepository extends JpaRepository<Dispatcher, Long> {

    // Specyficzne metody dla Dispatcher
    Optional<Dispatcher> findByServiceNumber(String serviceNumber);

    @Query("SELECT d FROM Dispatcher d WHERE SIZE(d.registeredIncidents) > 0")
    List<Dispatcher> findDispatchersWithIncidents();

    @Query("SELECT d FROM Dispatcher d LEFT JOIN d.shiftAssignments sa " +
            "WHERE sa IS NULL OR (sa.shift.endTime < CURRENT_TIMESTAMP) " +
            "ORDER BY d.id")
    List<Dispatcher> findAvailableDispatchers();

    Optional<Dispatcher> findFirstByOrderByIdAsc();
}