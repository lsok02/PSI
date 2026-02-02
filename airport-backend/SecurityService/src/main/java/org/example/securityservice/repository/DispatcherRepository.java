package org.example.securityservice.repository;

import org.example.securityservice.model.entity.Dispatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DispatcherRepository extends JpaRepository<Dispatcher, Long> {

    Optional<Dispatcher> findFirstByOrderByIdAsc();
}