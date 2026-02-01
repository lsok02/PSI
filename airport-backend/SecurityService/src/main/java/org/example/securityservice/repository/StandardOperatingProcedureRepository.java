package org.example.securityservice.repository;


import org.example.securityservice.model.entity.StandardOperatingProcedure;
import org.example.securityservice.model.enumeration.IncidentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StandardOperatingProcedureRepository extends JpaRepository<StandardOperatingProcedure, Long> {

    // Znajdź SOP po nazwie
    Optional<StandardOperatingProcedure> findByProcedureName(String procedureName);


    // Znajdź SOP dla danego typu incydentu
    @Query("SELECT sop FROM StandardOperatingProcedure sop " +
            "WHERE :incidentType MEMBER OF sop.applicableIncidentTypes")
    List<StandardOperatingProcedure> findByApplicableIncidentType(@Param("incidentType") IncidentType incidentType);

    List<StandardOperatingProcedure> findByApplicableIncidentTypesContaining(IncidentType incidentType);
}