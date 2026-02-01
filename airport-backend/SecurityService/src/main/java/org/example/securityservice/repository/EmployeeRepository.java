package org.example.securityservice.repository;

import org.example.securityservice.model.entity.Dispatcher;
import org.example.securityservice.model.entity.Employee;
import org.example.securityservice.model.entity.IncidentTeamMember;
import org.example.securityservice.model.entity.SecurityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByServiceNumber(String serviceNumber);

    List<Employee> findByFirstNameContainingIgnoreCase(String firstName);

    List<Employee> findByLastNameContainingIgnoreCase(String lastName);

    // Specjalne zapytanie dla konkretnych typów
    @Query("SELECT e FROM Employee e WHERE TYPE(e) = Dispatcher")
    List<Dispatcher> findAllDispatchers();

    @Query("SELECT e FROM Employee e WHERE TYPE(e) = SecurityManager")
    List<SecurityManager> findAllSecurityManagers();

    @Query("SELECT e FROM Employee e WHERE TYPE(e) = IncidentTeamMember")
    List<IncidentTeamMember> findAllTeamMembers();

    // Znajdź aktywnych Security Managers (np. na podstawie przypisania do zmiany)
    @Query("SELECT sm FROM SecurityManager sm " +
            "WHERE EXISTS (SELECT sa FROM ShiftAssignment sa " +
            "WHERE sa.employee = sm AND " +
            "sa.shift.startTime <= CURRENT_TIMESTAMP AND " +
            "sa.shift.endTime >= CURRENT_TIMESTAMP)")
    List<SecurityManager> findActiveSecurityManagers();

    Employee findByUsername(String employeeUsername);
}