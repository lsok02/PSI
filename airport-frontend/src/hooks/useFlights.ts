import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { flightApi } from '@/api';
import type { FlightStatus } from '@/types';

export const useFlights = (date?: string) => {
    return useQuery({
        queryKey: ['flights', date],
        queryFn: () => flightApi.getFlights(date),
        refetchInterval: 30000, // Refresh every 30 seconds
    });
};

export const useFlight = (id: number) => {
    return useQuery({
        queryKey: ['flight', id],
        queryFn: () => flightApi.getFlightById(id),
        enabled: !!id,
    });
};

export const useUpdateFlightStatus = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: ({ id, status }: { id: number; status: FlightStatus }) =>
            flightApi.updateFlightStatus(id, status),
        onSuccess: (data) => {
            queryClient.invalidateQueries({ queryKey: ['flights'] });
            queryClient.invalidateQueries({ queryKey: ['flight', data.id] });
        },
    });
};

export const useAvailableStatuses = (id: number) => {
    return useQuery({
        queryKey: ['flight-statuses', id],
        queryFn: () => flightApi.getAvailableStatuses(id),
        enabled: !!id,
    });
};
