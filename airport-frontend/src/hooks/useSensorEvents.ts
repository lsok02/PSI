import { useQuery } from '@tanstack/react-query';
import { sensorEventApi } from '@/api';
import type { SensorEventDTO, Alert } from '@/types';

// Transform backend SensorEventDTO to legacy Alert format for UI components
function transformSensorEvent(event: SensorEventDTO): Alert {
    const sensorTypeMap: Record<string, string> = {
        SMOKE_DETECTOR: 'Smoke detected',
        MOTION_SENSOR: 'Motion detected',
        ACCESS_CONTROL: 'Unauthorized access',
        CAMERA: 'Camera alert',
        TEMPERATURE: 'Temperature anomaly',
        PRESSURE: 'Pressure anomaly',
    };

    const description = `${sensorTypeMap[event.sensorType] || event.sensorType} at ${event.locationName || event.locationDetails || 'Unknown location'}`;

    return {
        id: `ALT-${event.id}`,
        description,
        timestamp: new Date(event.timestamp).toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' }),
    };
}

export function useSensorEvents() {
    return useQuery({
        queryKey: ['sensor-events', 'unassigned'],
        queryFn: async () => {
            const response = await sensorEventApi.getUnassigned();
            return response.map(transformSensorEvent);
        },
        staleTime: 10000, // 10 seconds - alerts are time-sensitive
        refetchInterval: 15000, // Auto-refetch every 15 seconds
    });
}

// Hook to get raw sensor events (for creating incidents)
export function useRawSensorEvents() {
    return useQuery({
        queryKey: ['sensor-events', 'unassigned', 'raw'],
        queryFn: () => sensorEventApi.getUnassigned(),
        staleTime: 10000,
        refetchInterval: 15000,
    });
}
