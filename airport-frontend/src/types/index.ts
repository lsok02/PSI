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
