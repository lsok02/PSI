import { useState } from 'react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { Badge } from '@/components/ui/badge';
import { Separator } from '@/components/ui/separator';
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select';
import { Plane, Clock, MapPin, AlertTriangle, Save, X } from 'lucide-react';
import type { Flight, FlightStatus } from '@/types';

interface FlightEditFormProps {
    flight: Flight;
    onSave: (updatedFlight: Flight) => void;
    onCancel: () => void;
}

const FLIGHT_STATUSES: FlightStatus[] = ['PLANNED', 'DELAYED', 'CANCELLED', 'DEPARTED', 'LANDED'];

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

export function FlightEditForm({ flight, onSave, onCancel }: FlightEditFormProps) {
    const [editedFlight, setEditedFlight] = useState<Flight>(flight);

    const handleStatusChange = (status: FlightStatus) => {
        setEditedFlight({ ...editedFlight, status });
    };

    const handleDelayChange = (value: string) => {
        setEditedFlight({ ...editedFlight, estimatedDelayMinutes: parseInt(value) || 0 });
    };

    const handleDelayReasonChange = (value: string) => {
        setEditedFlight({ ...editedFlight, delayReason: value });
    };

    const handleSave = () => {
        onSave(editedFlight);
    };

    return (
        <div className="space-y-6">
            {/* Flight Header */}
            <Card className="bg-slate-900 border-slate-800">
                <div className="p-6">
                    <div className="flex items-center justify-between mb-4">
                        <div className="flex items-center gap-3">
                            <div className="w-12 h-12 rounded-lg bg-blue-600 flex items-center justify-center">
                                <Plane className="w-6 h-6 text-white" />
                            </div>
                            <div>
                                <h2 className="text-2xl font-semibold text-slate-100">{flight.flightNumber}</h2>
                                <p className="text-slate-400">
                                    {flight.departureAirport
                                        ? `${flight.departureAirport} → ${flight.arrivalAirport}`
                                        : 'No route assigned'}
                                </p>
                            </div>
                        </div>
                        <Badge variant="outline" className={`${getStatusColor(editedFlight.status)} text-lg px-4 py-2`}>
                            {editedFlight.status}
                        </Badge>
                    </div>

                    <Separator className="bg-slate-800 my-4" />

                    {/* Flight Info Grid */}
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                        <div className="space-y-1">
                            <p className="text-slate-500 text-xs">Scheduled Departure</p>
                            <div className="flex items-center gap-2 text-slate-200">
                                <Clock className="w-4 h-4 text-blue-400" />
                                {new Date(flight.scheduledDepartureTime).toLocaleString()}
                            </div>
                        </div>
                        <div className="space-y-1">
                            <p className="text-slate-500 text-xs">Scheduled Arrival</p>
                            <div className="flex items-center gap-2 text-slate-200">
                                <Clock className="w-4 h-4 text-blue-400" />
                                {new Date(flight.scheduledArrivalTime).toLocaleString()}
                            </div>
                        </div>
                        <div className="space-y-1">
                            <p className="text-slate-500 text-xs">Gate</p>
                            <div className="flex items-center gap-2 text-slate-200">
                                <MapPin className="w-4 h-4 text-blue-400" />
                                {flight.terminal || flight.gateNumber ? `${flight.terminal}-${flight.gateNumber}` : 'Not assigned'}
                            </div>
                        </div>
                        <div className="space-y-1">
                            <p className="text-slate-500 text-xs">Aircraft</p>
                            <div className="flex items-center gap-2 text-slate-200">
                                <Plane className="w-4 h-4 text-blue-400" />
                                {flight.aircraftType || 'Not assigned'}
                            </div>
                        </div>
                    </div>
                </div>
            </Card>

            {/* Edit Form */}
            <Card className="bg-slate-900 border-slate-800">
                <div className="p-6">
                    <h3 className="text-slate-100 mb-4 flex items-center gap-2">
                        <AlertTriangle className="w-5 h-5 text-yellow-500" />
                        Update Flight Status
                    </h3>

                    <div className="space-y-6">
                        {/* Status Selection */}
                        <div className="space-y-2">
                            <Label className="text-slate-300">Flight Status</Label>
                            <Select value={editedFlight.status} onValueChange={handleStatusChange}>
                                <SelectTrigger className="bg-slate-950 border-slate-700 text-slate-100">
                                    <SelectValue />
                                </SelectTrigger>
                                <SelectContent className="bg-slate-900 border-slate-700">
                                    {FLIGHT_STATUSES.map((status) => (
                                        <SelectItem
                                            key={status}
                                            value={status}
                                            className="text-slate-200 focus:bg-slate-800 focus:text-slate-100"
                                        >
                                            <div className="flex items-center gap-2">
                                                <Badge variant="outline" className={`${getStatusColor(status)} text-xs`}>
                                                    {status}
                                                </Badge>
                                            </div>
                                        </SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>
                        </div>

                        {/* Delay Info (shown when DELAYED) */}
                        {editedFlight.status === 'DELAYED' && (
                            <>
                                <div className="space-y-2">
                                    <Label className="text-slate-300">Estimated Delay (minutes)</Label>
                                    <Input
                                        type="number"
                                        value={editedFlight.estimatedDelayMinutes || ''}
                                        onChange={(e) => handleDelayChange(e.target.value)}
                                        placeholder="Enter delay in minutes"
                                        className="bg-slate-950 border-slate-700 text-slate-100"
                                    />
                                </div>

                                <div className="space-y-2">
                                    <Label className="text-slate-300">Delay Reason</Label>
                                    <Textarea
                                        value={editedFlight.delayReason || ''}
                                        onChange={(e) => handleDelayReasonChange(e.target.value)}
                                        placeholder="Enter reason for delay..."
                                        rows={3}
                                        className="bg-slate-950 border-slate-700 text-slate-100"
                                    />
                                </div>
                            </>
                        )}

                        {/* Action Buttons */}
                        <div className="flex gap-3 pt-4">
                            <Button onClick={handleSave} className="bg-blue-600 hover:bg-blue-700 text-white">
                                <Save className="w-4 h-4 mr-2" />
                                Save Changes
                            </Button>
                            <Button onClick={onCancel} variant="outline" className="border-slate-700 text-slate-300 hover:bg-slate-800">
                                <X className="w-4 h-4 mr-2" />
                                Cancel
                            </Button>
                        </div>
                    </div>
                </div>
            </Card>
        </div>
    );
}
