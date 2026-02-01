// Security Dashboard Types (from backend)
export type IncidentStatus = 'NEW' | 'ASSIGNED' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED';
export type IncidentPriority = 'CRITICAL' | 'HIGH' | 'NORMAL' | 'LOW';
export type IncidentType =
    | 'MEDICAL_EMERGENCY' | 'MEDICAL_FIRST_AID' | 'MEDICAL_HEART_ATTACK' | 'MEDICAL_STROKE' | 'MEDICAL_ALLERGIC_REACTION'
    | 'TECHNICAL_POWER_OUTAGE' | 'TECHNICAL_EQUIPMENT_FAILURE' | 'TECHNICAL_IT_SYSTEM_FAILURE' | 'TECHNICAL_COMMUNICATION_FAILURE' | 'TECHNICAL_WATER_LEAKAGE'
    | 'SECURITY_UNAUTHORIZED_ACCESS' | 'SECURITY_SUSPICIOUS_PACKAGE' | 'SECURITY_TERRORIST_THREAT' | 'SECURITY_ASSAULT' | 'SECURITY_THEFT' | 'SECURITY_CYBER_ATTACK'
    | 'FIRE_ELECTRICAL' | 'FIRE_CHEMICAL' | 'FIRE_STRUCTURAL' | 'FIRE_VEHICLE' | 'HAZARDOUS_MATERIAL_SPILL'
    | 'EQUIPMENT_BAGGAGE_HANDLING' | 'EQUIPMENT_ELEVATOR_STUCK' | 'EQUIPMENT_ESCALATOR_FAILURE' | 'EQUIPMENT_DOOR_MALFUNCTION' | 'EQUIPMENT_SECURITY_SCANNER'
    | 'WEATHER_STORM' | 'WEATHER_SNOW_ICE' | 'WEATHER_FOG' | 'WEATHER_LIGHTNING'
    | 'OPERATIONAL_CROWD_CONTROL' | 'OPERATIONAL_DELAY_MANAGEMENT' | 'OPERATIONAL_NOISE_COMPLAINT' | 'OPERATIONAL_PARKING_ISSUE'
    | 'TRANSPORTATION_VEHICLE_ACCIDENT' | 'TRANSPORTATION_RUNWAY_INCIDENT' | 'TRANSPORTATION_TAXIWAY_INCIDENT' | 'TRANSPORTATION_AIRCRAFT_INCIDENT'
    | 'ENVIRONMENTAL_BIRD_STRIKE' | 'ENVIRONMENTAL_WILDLIFE_INTRUSION' | 'ENVIRONMENTAL_POLLUTION'
    | 'OTHER_MISCELLANEOUS' | 'OTHER_DRILL_EXERCISE' | 'OTHER_TEST_ALARM';

export interface LocationDTO {
    id: number;
    name: string;
    type: string;
    coordinates?: string;
}

export interface IncidentTeamDTO {
    id: number;
    teamName: string;
    specialization: string;
    status: string;
}

export interface LogEntryDTO {
    id: number;
    actionTime: string;
    actionDescription: string;
    details?: string;
    employeeName?: string;
}

// Backend Incident Response (from API)
export interface IncidentResponse {
    id: number;
    reportNumber: string;
    type: IncidentType;
    priority: IncidentPriority;
    status: IncidentStatus;
    description: string;
    creationTime: string;
    closureTime?: string;
    location?: LocationDTO;
    assignedTeam?: IncidentTeamDTO;
    journalEntries?: LogEntryDTO[];
}

// Create Incident Request (for POST)
export interface CreateIncidentRequest {
    type: IncidentType;
    priority: IncidentPriority;
    locationId: number;
    description: string;
    status?: IncidentStatus;
    assignedTeamId?: number;
    sensorEventId?: number; // Link to sensor event (alert) that triggered this incident
}

// Sensor Event Types (from backend)
export type SensorType = 'SMOKE_DETECTOR' | 'MOTION_SENSOR' | 'ACCESS_CONTROL' | 'CAMERA' | 'TEMPERATURE' | 'PRESSURE';

export interface SensorEventDTO {
    id: number;
    sensorId: string;
    sensorType: SensorType;
    locationDetails: string;
    timestamp: string;
    locationId?: number;
    locationName?: string;
    incidentId?: number;
    isProcessed: boolean;
}

// Legacy types for mock data (will be phased out)
export interface Incident {
    id: string;
    type: string;
    priority: 'Critical' | 'High' | 'Medium' | 'Low';
    location: string;
    status: string;
    assignedTeam: string;
    coordinates: { x: number; y: number };
    timestamp: string;
    description: string;
    timeline: Array<{ time: string; action: string }>;
}

export interface Alert {
    id: string;
    description: string;
    timestamp: string;
}

export interface ResponseTeam {
    id: string;
    name: string;
    type: 'medical' | 'security' | 'fire';
    coordinates: { x: number; y: number };
}

// Flight Management Types
export type FlightStatus = 'PLANNED' | 'DELAYED' | 'CANCELLED' | 'DEPARTED' | 'LANDED';

export interface Route {
    id: number;
    departureAirport: string;
    destinationAirport: string;
    routeCode: string;
    type: string;
}

export interface Gate {
    id: number;
    gateNumber: string;
    terminal: string;
    isAvailable: boolean;
}

export interface Aircraft {
    id: number;
    registrationNumber: string;
    model: string;
    capacity: number;
}

export interface Flight {
    id: number;
    flightNumber: string;
    scheduledDepartureTime: string;
    actualDepartureTime?: string;
    scheduledArrivalTime: string;
    actualArrivalTime?: string;
    status: FlightStatus;
    estimatedDelay?: number;
    delayReason?: string;
    route?: Route;
    gate?: Gate;
    aircraft?: Aircraft;
}
