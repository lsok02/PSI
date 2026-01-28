package org.example.flightservice.repository;

import org.example.flightservice.model.entity.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {
}