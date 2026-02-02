import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { groundOpsApi } from '@/api';
import type { FailureReportRequest } from '@/types';

export function useGroundResources() {
    return useQuery({
        queryKey: ['ground-resources'],
        queryFn: () => groundOpsApi.getResources(),
        staleTime: 60000,
    });
}

export function useFailures() {
    return useQuery({
        queryKey: ['ground-failures'],
        queryFn: () => groundOpsApi.getFailures(),
        staleTime: 30000,
    });
}

export function useReportFailure() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (data: FailureReportRequest) => groundOpsApi.createFailure(data),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['ground-failures'] });
            queryClient.invalidateQueries({ queryKey: ['ground-resources'] });
        },
        onError: (error) => {
            console.error('Failed to report failure:', error);
        },
    });
}
