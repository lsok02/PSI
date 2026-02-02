package org.example.securityservice.repository;


import org.example.securityservice.model.entity.StandardOperatingProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandardOperatingProcedureRepository extends JpaRepository<StandardOperatingProcedure, Long> {

}