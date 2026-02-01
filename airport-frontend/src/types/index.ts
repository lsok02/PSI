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
