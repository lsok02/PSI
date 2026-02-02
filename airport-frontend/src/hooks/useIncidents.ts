import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { securityApi } from '@/api';
import type { Incident, IncidentResponse, CreateIncidentRequest } from '@/types';

// Map location names to airport layout coordinates
function getLocationCoordinates(locationName: string): { x: number, y: number } {
    const name = locationName.toLowerCase();

    // Terminals
    if (name.includes('terminal a')) return { x: 22.5, y: 33 };
    if (name.includes('terminal b')) return { x: 22.5, y: 73 };
    if (name.includes('terminal c')) return { x: 52.5, y: 51 };

    // Gates
    if (name.includes('gate a')) return { x: 15, y: 25 };
    if (name.includes('gate b')) return { x: 15, y: 75 };
    if (name.includes('gate c')) return { x: 52, y: 40 };
    if (name.includes('gate g15')) return { x: 80, y: 24 };
    if (name.includes('gate g20')) return { x: 80, y: 36 };

    // Checkpoints
    if (name.includes('checkpoint c')) return { x: 45, y: 75 };
    if (name.includes('checkpoint')) return { x: 45, y: 70 };

    // Baggage
    if (name.includes('baggage')) return { x: 22.5, y: 88 };

    // Maintenance
    if (name.includes('maint') || name.includes('bay')) return { x: 82.5, y: 71 };

    // Runways & Taxiways
    if (name.includes('runway 09r')) return { x: 50, y: 10 };
    if (name.includes('runway 18')) return { x: 50, y: 95 };
    if (name.includes('runway')) return { x: 50, y: 5 };
    if (name.includes('taxiway')) return { x: 40, y: 15 };

    // Control Tower
    if (name.includes('tower')) return { x: 60, y: 30 };

    // Parking
    if (name.includes('parking')) return { x: 85, y: 85 };

    // Default with slight jitter to prevent exact overlap
    return {
        x: 50 + (Math.random() * 10 - 5),
        y: 50 + (Math.random() * 10 - 5)
    };
}

// Transform backend IncidentResponse to legacy Incident format for UI components
function transformIncident(response: IncidentResponse): Incident {
    const priorityMap: Record<string, 'Critical' | 'High' | 'Medium' | 'Low'> = {
        CRITICAL: 'Critical',
        HIGH: 'High',
        NORMAL: 'Medium',
        LOW: 'Low',
    };

    const typeMap: Record<string, string> = {
        MEDICAL_EMERGENCY: 'Medical Emergency',
        MEDICAL_FIRST_AID: 'First Aid',
        TECHNICAL_ISSUE: 'Technical Issue',
        TECHNICAL_POWER_OUTAGE: 'Power Outage',
        SECURITY_THREAT: 'Security Threat',
        SECURITY_UNAUTHORIZED_ACCESS: 'Unauthorized Access',
        FIRE_ALARM: 'Fire Alarm',
        FIRE_ELECTRICAL: 'Electrical Fire',
        EQUIPMENT_FAILURE: 'Equipment Failure',
        OTHER: 'Other',
    };

    const statusMap: Record<string, string> = {
        NEW: 'New',
        ASSIGNED: 'Assigned',
        IN_PROGRESS: 'In Progress',
        RESOLVED: 'Resolved',
        CLOSED: 'Closed',
    };

    const locationName = response.location?.name || 'Unknown Location';

    return {
        id: `INC-${response.id.toString().padStart(4, '0')}`,
        type: typeMap[response.type] || response.type.replace(/_/g, ' '),
        priority: priorityMap[response.priority] || 'Medium',
        location: locationName,
        status: statusMap[response.status] || response.status,
        assignedTeam: response.assignedTeam?.teamName || 'Unassigned',
        reportSource: response.reportSource,
        coordinates: getLocationCoordinates(locationName),
        timestamp: new Date(response.creationTime).toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' }),
        description: response.description,
        timeline: response.journalEntries?.map(entry => ({
            time: new Date(entry.actionTime).toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' }),
            action: entry.actionDescription,
        })) || [],
    };
}

// Hook to fetch all incidents
export function useIncidents(filters?: { status?: string; priority?: string; type?: string }) {
    return useQuery({
        queryKey: ['incidents', filters],
        queryFn: async () => {
            const response = await securityApi.getIncidents(filters as any);
            return response.map(transformIncident);
        },
        staleTime: 30000, // 30 seconds
        refetchInterval: 60000, // Auto-refetch every minute
    });
}

// Hook to create a new incident
export function useCreateIncident() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (data: CreateIncidentRequest) => securityApi.createIncident(data),
        onSuccess: () => {
            // Invalidate queries to refresh data after creating
            queryClient.invalidateQueries({ queryKey: ['incidents'] });
            queryClient.invalidateQueries({ queryKey: ['sensor-events'] });
        },
        onError: (error) => {
            console.error('Failed to create incident:', error);
        },
    });
}

// Hook to fetch teams by specialization
export function useTeams(specialization?: string) {
    return useQuery({
        queryKey: ['teams', specialization],
        queryFn: () => securityApi.getTeamsBySpecialization(specialization!),
        enabled: !!specialization,
        staleTime: 60000,
    });
}

// Hook to escalate an incident
export function useEscalateIncident() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (incidentId: number) => securityApi.escalateIncident(incidentId),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['incidents'] });
        },
        onError: (error) => {
            console.error('Failed to escalate incident:', error);
        },
    });
}
// Hook to assign a team to an incident
export function useAssignTeam() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({ incidentId, teamId }: { incidentId: number; teamId: number }) =>
            securityApi.assignTeam(incidentId, teamId),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['incidents'] });
            queryClient.invalidateQueries({ queryKey: ['teams'] });
        },
        onError: (error) => {
            console.error('Failed to assign team:', error);
        },
    });
}

// Hook to update incident status (close/resolve)
export function useUpdateIncidentStatus() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({ incidentId, status, notes }: { incidentId: number; status: string; notes?: string }) =>
            securityApi.updateIncidentStatus(incidentId, status, notes),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['incidents'] });
            queryClient.invalidateQueries({ queryKey: ['teams'] });
        },
        onError: (error) => {
            console.error('Failed to update incident status:', error);
        },
    });
}
