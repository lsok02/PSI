package org.example.securityservice.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.securityservice.model.enumeration.IncidentPriority;
import org.example.securityservice.model.enumeration.IncidentStatus;
import org.example.securityservice.model.enumeration.IncidentType;
import org.example.securityservice.model.enumeration.ReportingSource;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reportNumber;
    private LocalDateTime reportTime;
    private LocalDateTime closeTime;
    private String description;

    @Enumerated(EnumType.STRING)
    private IncidentType type;
    @Enumerated(EnumType.STRING)
    private IncidentPriority priority;
    @Enumerated(EnumType.STRING)
    private IncidentStatus status;
    @Enumerated(EnumType.STRING)
    private ReportingSource source;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "dispatcher_id")
    private Dispatcher registeredBy;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private IncidentTeam assignedTeam;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private SecurityManager escalatedTo;

    @ManyToOne
    @JoinColumn(name = "sop_id")
    private StandardOperatingProcedure sop;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL)
    private List<LogEntry> history;

    @ManyToMany
    @JoinTable(name = "incident_resources")
    private List<AirportResource> affectedResources;
}