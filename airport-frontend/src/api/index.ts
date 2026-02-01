import axios, { type AxiosInstance } from 'axios';

// API Gateway URL - all requests go through the gateway
const API_GATEWAY_URL = import.meta.env.VITE_API_GATEWAY_URL || 'http://localhost:9090';

// Token storage key
const TOKEN_KEY = 'auth_token';

// Get stored token
export const getToken = () => localStorage.getItem(TOKEN_KEY);

// Set token
export const setToken = (token: string) => localStorage.setItem(TOKEN_KEY, token);

// Remove token
export const removeToken = () => localStorage.removeItem(TOKEN_KEY);

// Create axios instance with auth interceptor
function createApiClient(baseURL: string): AxiosInstance {
    const client = axios.create({
        baseURL,
        timeout: 10000,
        headers: {
            'Content-Type': 'application/json',
        },
    });

    // Request interceptor - add auth token
    client.interceptors.request.use(
        (config) => {
            const token = getToken();
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
                config.headers['token'] = token; // Add custom token header for Security Service
            }
            return config;
        },
        (error) => Promise.reject(error)
    );

    // Response interceptor - handle errors
    client.interceptors.response.use(
        (response) => response,
        (error) => {
            if (error.response) {
                switch (error.response.status) {
                    case 401:
                        // Clear token and redirect to login
                        removeToken();
                        if (window.location.pathname !== '/login') {
                            window.location.href = '/login';
                        }
                        break;
                    case 403:
                        console.error('Forbidden - you do not have permission');
                        break;
                    case 404:
                        console.error('Resource not found');
                        break;
                    case 500:
                        console.error('Server error');
                        break;
                }
            } else if (error.request) {
                console.error('Network error - server may be offline');
            }
            return Promise.reject(error);
        }
    );

    return client;
}

// Single API client through gateway
const api = createApiClient(API_GATEWAY_URL);

// Auth API (no token required for login)
export interface LoginRequest {
    username: string;
    password: string;
}

export interface LoginResponse {
    token: string | null;
    message: string;
}

export const authApi = {
    login: async (credentials: LoginRequest): Promise<LoginResponse> => {
        const response = await api.post<LoginResponse>('/api/auth/login', credentials);
        if (response.data.token) {
            setToken(response.data.token);
        }
        return response.data;
    },
    logout: () => {
        removeToken();
    },
    isAuthenticated: () => !!getToken(),
};

import type { Flight, FlightStatus } from '@/types';

// Flight Service API (through gateway)
export const flightApi = {
    hello: () => api.get<string>('/api/flights/hello').then(res => res.data),
    getFlights: (date?: string) => {
        const url = date ? `/api/flights?date=${date}` : '/api/flights';
        return api.get<Flight[]>(url).then(res => res.data);
    },
    getFlightById: (id: number) =>
        api.get<Flight>(`/api/flights/${id}`).then(res => res.data),
    updateFlightStatus: (id: number, status: FlightStatus) =>
        api.put<Flight>(`/api/flights/${id}/status`, { status }).then(res => res.data),
    getAvailableStatuses: (id: number) =>
        api.get<any>(`/api/flights/${id}/available-statuses`).then(res => res.data),
};

// Passenger Service API (through gateway)
export const passengerApi = {
    hello: () => api.get<string>('/api/passengers/hello').then(res => res.data),
    // getPassengers: () => api.get<Passenger[]>('/api/passengers').then(res => res.data),
};

// Security Service API (through gateway)
import type { IncidentResponse, IncidentStatus, IncidentPriority, IncidentType, CreateIncidentRequest } from '@/types';

export interface IncidentFilters {
    status?: IncidentStatus;
    priority?: IncidentPriority;
    type?: IncidentType;
}

export const securityApi = {
    getIncidents: (filters?: IncidentFilters) => {
        const params = new URLSearchParams();
        if (filters?.status) params.append('status', filters.status);
        if (filters?.priority) params.append('priority', filters.priority);
        if (filters?.type) params.append('type', filters.type);
        const queryString = params.toString();
        const url = queryString ? `/api/security/incidents?${queryString}` : '/api/security/incidents';
        return api.get<IncidentResponse[]>(url).then(res => res.data);
    },
    getIncidentById: (id: number) =>
        api.get<IncidentResponse>(`/api/security/incidents/${id}`).then(res => res.data),
    createIncident: (data: CreateIncidentRequest) =>
        api.post<IncidentResponse>('/api/security/incidents', data).then(res => res.data),
    getTeamsBySpecialization: (specialization: string) =>
        api.get<any[]>(`/api/security/teams?specialization=${specialization}`).then(res => res.data),
};

// Sensor Events API (through gateway)
import type { SensorEventDTO } from '@/types';

export const sensorEventApi = {
    getUnassigned: () =>
        api.get<SensorEventDTO[]>('/api/sensor-events/unassigned').then(res => res.data),
    createRandomAlarm: () =>
        api.post<SensorEventDTO>('/api/sensor-events/random-alarm').then(res => res.data),
    simulateAlarms: (count: number = 5) =>
        api.post<SensorEventDTO[]>(`/api/sensor-events/simulate-alarms?count=${count}`).then(res => res.data),
};

// Ground Operations Service API (through gateway)
export const groundOpsApi = {
    // getTeams: () => api.get<ResponseTeam[]>('/api/groundops/teams').then(res => res.data),
};

// Export the main API client for advanced usage
export { api };
