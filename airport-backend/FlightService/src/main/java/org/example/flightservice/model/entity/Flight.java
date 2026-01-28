package org.example.flightservice.model.entity;

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
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.flightservice.model.enumeration.FlightStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flightNumber;
    private LocalDateTime scheduledDepartureTime;
    private LocalDateTime actualDepartureTime;
    private LocalDateTime scheduledArrivalTime;
    private LocalDateTime actualArrivalTime;

    @Enumerated(EnumType.STRING)
    private FlightStatus status;

    private Integer estimatedDelay;
    private String delayReason;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private FlightSchedule schedule;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @OneToOne
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;

    @ManyToOne
    @JoinColumn(name = "aircraft_id")
    private Aircraft aircraft;

    @ManyToOne
    @JoinColumn(name = "runway_id")
    private Runway runway;

    @ManyToOne
    @JoinColumn(name = "gate_id")
    private Gate gate;

    @ManyToOne
    @JoinColumn(name = "parking_stand_id")
    private ParkingStand parkingStand;

    @ManyToOne
    @JoinColumn(name = "turnaround_process_id")
    private TurnaroundProcess turnaroundProcess;

    @ManyToMany
    @JoinTable(
            name = "flight_crew",
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "crew_member_id")
    )
    private List<CrewMember> crew;
}