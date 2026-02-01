import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { securityApi } from '@/api';
import type { Incident, IncidentResponse, CreateIncidentRequest } from '@/types';

// Transform backend IncidentResponse to legacy Incident format for UI components
function transformIncident(response: IncidentResponse): Incident {
    const priorityMap: Record<string, 'Critical' | 'High' | 'Medium' | 'Low'> = {
        CRITICAL: 'Critical',
        HIGH: 'High',
        NORMAL: 'Medium',
        LOW: 'Low',
    };

    const typeMap: Record<string, string> = {
        MEDICAL: 'Medical Emergency',
        TECHNICAL: 'Technical Issue',
        SECURITY_THREAT: 'Security Threat',
        FIRE: 'Fire Alarm',
        EQUIPMENT: 'Equipment Failure',
        OTHER: 'Other',
    };

    const statusMap: Record<string, string> = {
        NEW: 'New',
        ASSIGNED: 'Assigned',
        IN_PROGRESS: 'In Progress',
        RESOLVED: 'Resolved',
        CLOSED: 'Closed',
    };

    return {
        id: `INC-${response.id.toString().padStart(4, '0')}`,
        type: typeMap[response.type] || response.type,
        priority: priorityMap[response.priority] || 'Medium',
        location: response.location?.name || 'Unknown Location',
        status: statusMap[response.status] || response.status,
        assignedTeam: response.assignedTeam?.teamName || 'Unassigned',
        coordinates: { x: 50, y: 50 }, // Default coordinates
        timestamp: new Date(response.creationTime).toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' }),
        description: response.description,
        timeline: response.journalEntries?.map(entry => ({
            time: new Date(entry.actionTime).toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' }),
            action: entry.actionDescription,
        })) || [],
    };
}

// Hook to fetch all incidents
export function useIncidents() {
    return useQuery({
        queryKey: ['incidents'],
        queryFn: async () => {
            const response = await securityApi.getIncidents();
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
