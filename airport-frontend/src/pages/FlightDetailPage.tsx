import { useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { Shield, Plane, ArrowLeft } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { FlightEditForm } from '@/components/flights';
import type { Flight } from '@/types';

// Mock data - same as FlightsPage (in real app, would fetch by ID)
const mockFlights: Flight[] = [
    {
        id: 1,
        flightNumber: 'LO 3841',
        scheduledDepartureTime: '2026-02-01T14:30:00',
        scheduledArrivalTime: '2026-02-01T16:45:00',
        status: 'PLANNED',
        route: { id: 1, departureAirport: 'WAW', destinationAirport: 'LHR', routeCode: 'WAW-LHR', type: 'International' },
        gate: { id: 1, gateNumber: 'A12', terminal: 'T1', isAvailable: false },
        aircraft: { id: 1, registrationNumber: 'SP-LRA', model: 'Boeing 787-8', capacity: 252 },
    },
    {
        id: 2,
        flightNumber: 'LO 281',
        scheduledDepartureTime: '2026-02-01T15:15:00',
        scheduledArrivalTime: '2026-02-01T17:30:00',
        status: 'DELAYED',
        estimatedDelay: 45,
        delayReason: 'Weather conditions at destination',
        route: { id: 2, departureAirport: 'WAW', destinationAirport: 'FRA', routeCode: 'WAW-FRA', type: 'International' },
        gate: { id: 2, gateNumber: 'B08', terminal: 'T1', isAvailable: false },
        aircraft: { id: 2, registrationNumber: 'SP-LRB', model: 'Embraer E195', capacity: 120 },
    },
    {
        id: 3,
        flightNumber: 'FR 4521',
        scheduledDepartureTime: '2026-02-01T13:00:00',
        scheduledArrivalTime: '2026-02-01T15:20:00',
        status: 'DEPARTED',
        actualDepartureTime: '2026-02-01T13:05:00',
        route: { id: 3, departureAirport: 'WAW', destinationAirport: 'BCN', routeCode: 'WAW-BCN', type: 'International' },
        gate: { id: 3, gateNumber: 'C15', terminal: 'T2', isAvailable: true },
        aircraft: { id: 3, registrationNumber: 'EI-DWE', model: 'Boeing 737-800', capacity: 189 },
    },
    {
        id: 4,
        flightNumber: 'LO 529',
        scheduledDepartureTime: '2026-02-01T11:30:00',
        scheduledArrivalTime: '2026-02-01T13:45:00',
        status: 'LANDED',
        actualDepartureTime: '2026-02-01T11:35:00',
        actualArrivalTime: '2026-02-01T13:50:00',
        route: { id: 4, departureAirport: 'WAW', destinationAirport: 'CDG', routeCode: 'WAW-CDG', type: 'International' },
        gate: { id: 4, gateNumber: 'A05', terminal: 'T1', isAvailable: true },
        aircraft: { id: 4, registrationNumber: 'SP-LRC', model: 'Boeing 787-9', capacity: 294 },
    },
    {
        id: 5,
        flightNumber: 'W6 1234',
        scheduledDepartureTime: '2026-02-01T16:00:00',
        scheduledArrivalTime: '2026-02-01T18:30:00',
        status: 'CANCELLED',
        delayReason: 'Technical issues with aircraft',
        route: { id: 5, departureAirport: 'WAW', destinationAirport: 'FCO', routeCode: 'WAW-FCO', type: 'International' },
        gate: { id: 5, gateNumber: 'D03', terminal: 'T2', isAvailable: true },
    },
    {
        id: 6,
        flightNumber: 'LO 7051',
        scheduledDepartureTime: '2026-02-01T17:45:00',
        scheduledArrivalTime: '2026-02-01T19:00:00',
        status: 'PLANNED',
        route: { id: 6, departureAirport: 'WAW', destinationAirport: 'KRK', routeCode: 'WAW-KRK', type: 'Domestic' },
        gate: { id: 6, gateNumber: 'B12', terminal: 'T1', isAvailable: false },
        aircraft: { id: 5, registrationNumber: 'SP-LDE', model: 'Embraer E170', capacity: 76 },
    },
];

export function FlightDetailPage() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

    const flight = mockFlights.find((f) => f.id === Number(id));
    const [currentFlight, setCurrentFlight] = useState<Flight | undefined>(flight);

    if (!currentFlight) {
        return (
            <div className="h-screen bg-slate-950 text-slate-100 flex items-center justify-center">
                <div className="text-center">
                    <h1 className="text-2xl font-semibold mb-4">Flight not found</h1>
                    <Button onClick={() => navigate('/flights')} variant="outline">
                        Back to Flights
                    </Button>
                </div>
            </div>
        );
    }

    const handleSave = (updatedFlight: Flight) => {
        setCurrentFlight(updatedFlight);
        console.log('Saving flight:', updatedFlight);
        // In real app, would call API here
        navigate('/flights');
    };

    const handleCancel = () => {
        navigate('/flights');
    };

    return (
        <div className="h-screen bg-slate-950 text-slate-100 flex flex-col overflow-hidden">
            {/* Navigation Header */}
            <header className="shrink-0 border-b border-slate-800 bg-slate-900">
                <div className="px-6 py-4 flex items-center justify-between">
                    <h1 className="text-xl font-semibold text-slate-100">Airport Management System</h1>
                    <nav className="flex items-center gap-4">
                        <Link
                            to="/security"
                            className="flex items-center gap-2 px-4 py-2 rounded-md text-slate-400 hover:text-slate-100 hover:bg-slate-800 transition-colors"
                        >
                            <Shield className="w-4 h-4" />
                            Security
                        </Link>
                        <Link
                            to="/flights"
                            className="flex items-center gap-2 px-4 py-2 rounded-md bg-blue-600 text-white"
                        >
                            <Plane className="w-4 h-4" />
                            Flights
                        </Link>
                    </nav>
                </div>
            </header>

            {/* Main Content */}
            <main className="flex-1 min-h-0 p-6 overflow-auto">
                <div className="max-w-4xl mx-auto">
                    {/* Back Button */}
                    <Button
                        onClick={() => navigate('/flights')}
                        variant="ghost"
                        className="mb-4 text-slate-400 hover:text-slate-100"
                    >
                        <ArrowLeft className="w-4 h-4 mr-2" />
                        Back to Flight List
                    </Button>

                    <FlightEditForm
                        flight={currentFlight}
                        onSave={handleSave}
                        onCancel={handleCancel}
                    />
                </div>
            </main>
        </div>
    );
}
