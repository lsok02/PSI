import { useNavigate } from 'react-router-dom';
import { Plane, Clock, MapPin } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from '@/components/ui/table';
import type { Flight, FlightStatus } from '@/types';

interface FlightsTableProps {
    flights: Flight[];
}

const getStatusColor = (status: FlightStatus) => {
    switch (status) {
        case 'LANDED':
            return 'bg-green-950 text-green-300 border-green-800';
        case 'DEPARTED':
            return 'bg-blue-950 text-blue-300 border-blue-800';
        case 'DELAYED':
            return 'bg-yellow-950 text-yellow-300 border-yellow-800';
        case 'CANCELLED':
            return 'bg-red-950 text-red-300 border-red-800';
        case 'PLANNED':
        default:
            return 'bg-slate-800 text-slate-300 border-slate-700';
    }
};

const formatTime = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', hour12: false });
};

const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
};

export function FlightsTable({ flights }: FlightsTableProps) {
    const navigate = useNavigate();

    const handleRowClick = (flightId: number) => {
        navigate(`/flights/${flightId}`);
    };

    return (
        <Card className="bg-slate-900 border-slate-800">
            <div className="p-4 border-b border-slate-800">
                <div className="flex items-center gap-2">
                    <Plane className="w-5 h-5 text-blue-400" />
                    <h2 className="text-slate-100">Flight Schedule ({flights.length})</h2>
                </div>
            </div>

            <Table>
                <TableHeader>
                    <TableRow className="border-slate-800 hover:bg-transparent">
                        <TableHead className="text-slate-400">Flight</TableHead>
                        <TableHead className="text-slate-400">Route</TableHead>
                        <TableHead className="text-slate-400">Departure</TableHead>
                        <TableHead className="text-slate-400">Arrival</TableHead>
                        <TableHead className="text-slate-400">Gate</TableHead>
                        <TableHead className="text-slate-400">Aircraft</TableHead>
                        <TableHead className="text-slate-400">Status</TableHead>
                        <TableHead className="text-slate-400">Delay</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {flights.map((flight) => (
                        <TableRow
                            key={flight.id}
                            onClick={() => handleRowClick(flight.id)}
                            className="border-slate-800 cursor-pointer hover:bg-slate-800/50 transition-colors"
                        >
                            <TableCell className="text-slate-100 font-medium">
                                <div className="flex items-center gap-2">
                                    <Plane className="w-4 h-4 text-blue-400" />
                                    {flight.flightNumber}
                                </div>
                            </TableCell>
                            <TableCell className="text-slate-300">
                                {flight.departureAirport ? (
                                    <div className="flex items-center gap-1">
                                        <span>{flight.departureAirport}</span>
                                        <span className="text-slate-500">→</span>
                                        <span>{flight.arrivalAirport}</span>
                                    </div>
                                ) : (
                                    <span className="text-slate-500">—</span>
                                )}
                            </TableCell>
                            <TableCell className="text-slate-300">
                                <div className="flex items-center gap-1">
                                    <Clock className="w-3 h-3 text-slate-500" />
                                    <span>{formatTime(flight.scheduledDepartureTime)}</span>
                                    <span className="text-slate-500 text-xs">{formatDate(flight.scheduledDepartureTime)}</span>
                                </div>
                            </TableCell>
                            <TableCell className="text-slate-300">
                                <div className="flex items-center gap-1">
                                    <Clock className="w-3 h-3 text-slate-500" />
                                    <span>{formatTime(flight.scheduledArrivalTime)}</span>
                                    <span className="text-slate-500 text-xs">{formatDate(flight.scheduledArrivalTime)}</span>
                                </div>
                            </TableCell>
                            <TableCell className="text-slate-300">
                                {flight.terminal || flight.gateNumber ? (
                                    <div className="flex items-center gap-1">
                                        <MapPin className="w-3 h-3 text-slate-500" />
                                        <span>{flight.terminal}-{flight.gateNumber}</span>
                                    </div>
                                ) : (
                                    <span className="text-slate-500">—</span>
                                )}
                            </TableCell>
                            <TableCell className="text-slate-300">
                                {flight.aircraftType ? (
                                    <span>{flight.aircraftType}</span>
                                ) : (
                                    <span className="text-slate-500">—</span>
                                )}
                            </TableCell>
                            <TableCell>
                                <Badge variant="outline" className={getStatusColor(flight.status)}>
                                    {flight.status}
                                </Badge>
                            </TableCell>
                            <TableCell className="text-slate-300">
                                {flight.estimatedDelayMinutes && flight.estimatedDelayMinutes > 0 ? (
                                    <span className="text-yellow-400">+{flight.estimatedDelayMinutes} min</span>
                                ) : (
                                    <span className="text-green-400">On time</span>
                                )}
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </Card>
    );
}
