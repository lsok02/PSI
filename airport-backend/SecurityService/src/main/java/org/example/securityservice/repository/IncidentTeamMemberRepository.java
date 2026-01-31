package org.example.securityservice.repository;

import org.example.securityservice.model.entity.IncidentTeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentTeamMemberRepository extends JpaRepository<IncidentTeamMember, Long> {

}