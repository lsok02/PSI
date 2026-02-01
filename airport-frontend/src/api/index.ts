import axios, { type AxiosInstance } from 'axios';

// Base API configuration
// Configure these based on your backend services or via environment variables
export const API_CONFIG = {
    FLIGHT_SERVICE: import.meta.env.VITE_FLIGHT_SERVICE_URL || 'http://localhost:8081',
    PASSENGER_SERVICE: import.meta.env.VITE_PASSENGER_SERVICE_URL || 'http://localhost:8082',
    SECURITY_SERVICE: import.meta.env.VITE_SECURITY_SERVICE_URL || 'http://localhost:8083',
    GROUND_OPS_SERVICE: import.meta.env.VITE_GROUND_OPS_SERVICE_URL || 'http://localhost:8084',
};

// Create axios instance with default config
function createApiClient(baseURL: string): AxiosInstance {
    const client = axios.create({
        baseURL,
        timeout: 10000,
        headers: {
            'Content-Type': 'application/json',
        },
    });

    // Request interceptor - useful for adding auth tokens later
    client.interceptors.request.use(
        (config) => {
            // You can add auth token here when you implement authentication
            // const token = localStorage.getItem('token');
            // if (token) {
            //   config.headers.Authorization = `Bearer ${token}`;
            // }
            return config;
        },
        (error) => Promise.reject(error)
    );

    // Response interceptor - centralized error handling
    client.interceptors.response.use(
        (response) => response,
        (error) => {
            // Handle specific error codes
            if (error.response) {
                switch (error.response.status) {
                    case 401:
                        // Handle unauthorized - redirect to login, etc.
                        console.error('Unauthorized - please log in');
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

// Create API clients for each service
const flightClient = createApiClient(API_CONFIG.FLIGHT_SERVICE);
const passengerClient = createApiClient(API_CONFIG.PASSENGER_SERVICE);
const securityClient = createApiClient(API_CONFIG.SECURITY_SERVICE);
const groundOpsClient = createApiClient(API_CONFIG.GROUND_OPS_SERVICE);

// Flight Service API
export const flightApi = {
    hello: () => flightClient.get<string>('/flights/hello').then(res => res.data),

    // Add more endpoints as the backend grows
    // getFlights: () => flightClient.get<Flight[]>('/flights').then(res => res.data),
    // getFlightById: (id: string) => flightClient.get<Flight>(`/flights/${id}`).then(res => res.data),
    // createFlight: (data: CreateFlightDto) => flightClient.post<Flight>('/flights', data).then(res => res.data),
    // updateFlight: (id: string, data: UpdateFlightDto) => flightClient.put<Flight>(`/flights/${id}`, data).then(res => res.data),
    // deleteFlight: (id: string) => flightClient.delete(`/flights/${id}`),
};

// Passenger Service API
export const passengerApi = {
    hello: () => passengerClient.get<string>('/passengers/hello').then(res => res.data),

    // Add more endpoints as the backend grows
    // getPassengers: () => passengerClient.get<Passenger[]>('/passengers').then(res => res.data),
};

// Security Service API (for future incident integration)
export const securityApi = {
    // Will be implemented when backend security endpoints are available
    // getIncidents: () => securityClient.get<Incident[]>('/incidents').then(res => res.data),
    // createIncident: (data: CreateIncidentDto) => securityClient.post<Incident>('/incidents', data).then(res => res.data),
    // updateIncident: (id: string, data: UpdateIncidentDto) => securityClient.put<Incident>(`/incidents/${id}`, data).then(res => res.data),
};

// Ground Operations Service API
export const groundOpsApi = {
    // Will be implemented when backend ground ops endpoints are available
    // getTeams: () => groundOpsClient.get<ResponseTeam[]>('/teams').then(res => res.data),
};

// Export clients for advanced usage
export { flightClient, passengerClient, securityClient, groundOpsClient };
