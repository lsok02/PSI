import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Shield, Plane, Wrench, LogOut, User } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useAuth } from '@/context';
import { useIncidents, useSensorEvents, useRawSensorEvents } from '@/hooks';
import {
    IncidentsList,
    AirportMap,
    AlertsPanel,
    IncidentDetails,
    CreateIncidentModal
} from '@/components/security';

// Main Security Dashboard Page

export function SecurityDashboardPage() {
    const [selectedIncidentId, setSelectedIncidentId] = useState<string | null>(null);
    const [createModalOpen, setCreateModalOpen] = useState(false);
    const [selectedAlertData, setSelectedAlertData] = useState<{
        sourceSystem: string;
        detectionTime: string;
        sensorId: string;
        suggestedType?: string;
        suggestedPriority?: string;
        suggestedLocation?: string;
        sensorEventId?: number;
    } | undefined>(undefined);
    const { logout, username } = useAuth();
    const navigate = useNavigate();

    const [incidentFilters, setIncidentFilters] = useState({
        priority: 'CRITICAL', // Default to urgent incidents
        status: 'all'
    });

    // Fetch incidents from backend with filters
    const { data: backendIncidents } = useIncidents({
        status: incidentFilters.status === 'all' ? undefined : incidentFilters.status,
        priority: incidentFilters.priority === 'all' ? undefined : incidentFilters.priority
    });
    const incidents = backendIncidents || [];

    // Set default filter to "Urgent" (Critical/High) on first load if no priority is set
    useEffect(() => {
        // Only set default if we haven't manually changed it yet
        // In a real app we might want a more sophisticated "Urgent" view that combines multiple priorities
        // For now, let's just default to 'all' but allow the user to easily filter.
        // Actually the user asked for "more urgent", so let's default to CRITICAL first.
        // setIncidentFilters(prev => ({ ...prev, priority: 'CRITICAL' }));
    }, []);

    // Fetch alerts (unassigned sensor events) from backend
    const { data: backendAlerts } = useSensorEvents();
    const alerts = backendAlerts || [];

    // Raw sensor events for getting full data when creating incident
    const { data: rawSensorEvents = [] } = useRawSensorEvents();

    // Select first incident by default when loaded or if current selection is filtered out
    useEffect(() => {
        if (incidents.length > 0) {
            const currentStillExists = incidents.some(inc => inc.id === selectedIncidentId);
            if (!selectedIncidentId || !currentStillExists) {
                setSelectedIncidentId(incidents[0].id);
            }
        } else {
            setSelectedIncidentId(null);
        }
    }, [incidents, selectedIncidentId]);

    const selectedIncident = incidents.find(inc => inc.id === selectedIncidentId);

    // Map sensor type to suggested incident type
    const getSuggestedType = (sensorType: string): string => {
        const typeMap: Record<string, string> = {
            SMOKE_DETECTOR: 'Fire Alarm',
            MOTION_SENSOR: 'Security Breach',
            ACCESS_CONTROL: 'Unauthorized Access',
            CAMERA: 'Security Breach',
            TEMPERATURE: 'Technical Failure',
            PRESSURE: 'Technical Failure',
            FIRE: 'Fire Alarm',
            SMOKE: 'Fire Alarm',
        };
        return typeMap[sensorType] || 'Technical Failure';
    };

    // Map sensor type to suggested priority
    const getSuggestedPriority = (sensorType: string): string => {
        const priorityMap: Record<string, string> = {
            SMOKE_DETECTOR: 'Critical',
            FIRE: 'Critical',
            SMOKE: 'Critical',
            ACCESS_CONTROL: 'High',
            MOTION_SENSOR: 'Medium',
            CAMERA: 'Medium',
            TEMPERATURE: 'Low',
            PRESSURE: 'Low',
        };
        return priorityMap[sensorType] || 'Medium';
    };

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const handleIncidentSelect = (incidentId: string) => {
        setSelectedIncidentId(incidentId);
    };

    const handleCreateIncident = (alertId: string) => {
        // Extract numeric ID from alert ID (e.g., "ALT-123" -> 123)
        const numericId = parseInt(alertId.replace('ALT-', ''), 10);

        console.log('handleCreateIncident called with alertId:', alertId, 'numericId:', numericId);
        console.log('rawSensorEvents:', rawSensorEvents);

        // Find raw sensor event data
        const sensorEvent = rawSensorEvents?.find(e => e.id === numericId);

        console.log('Found sensorEvent:', sensorEvent);

        if (sensorEvent) {
            setSelectedAlertData({
                sourceSystem: sensorEvent.sensorType,
                detectionTime: new Date(sensorEvent.timestamp).toLocaleString(),
                sensorId: sensorEvent.sensorId,
                suggestedType: getSuggestedType(sensorEvent.sensorType),
                suggestedPriority: getSuggestedPriority(sensorEvent.sensorType),
                suggestedLocation: sensorEvent.locationName || sensorEvent.locationDetails || 'Unknown location',
                sensorEventId: sensorEvent.id,
            });
        } else {
            setSelectedAlertData(undefined);
        }

        setCreateModalOpen(true);
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
                            className="flex items-center gap-2 px-4 py-2 rounded-md bg-blue-600 text-white"
                        >
                            <Shield className="w-4 h-4" />
                            Security
                        </Link>
                        <Link
                            to="/flights"
                            className="flex items-center gap-2 px-4 py-2 rounded-md text-slate-400 hover:text-slate-100 hover:bg-slate-800 transition-colors"
                        >
                            <Plane className="w-4 h-4" />
                            Flights
                        </Link>
                        <Link
                            to="/ground-ops"
                            className="flex items-center gap-2 px-4 py-2 rounded-md text-slate-400 hover:text-slate-100 hover:bg-slate-800 transition-colors"
                        >
                            <Wrench className="w-4 h-4" />
                            Zasoby
                        </Link>
                        <div className="h-6 w-px bg-slate-700" />
                        <div className="flex items-center gap-2 text-slate-400 text-sm">
                            <User className="w-4 h-4" />
                            <span>{username || 'User'}</span>
                        </div>
                        <Button
                            onClick={handleLogout}
                            variant="ghost"
                            size="sm"
                            className="text-slate-400 hover:text-red-400 hover:bg-slate-800"
                        >
                            <LogOut className="w-4 h-4" />
                        </Button>
                    </nav>
                </div>
            </header>

            {/* Main Content */}
            <main className="flex-1 min-h-0 p-4 flex flex-col">
                <div className="grid grid-cols-12 gap-4 flex-1 min-h-0">
                    {/* Left Column: Active Incidents */}
                    <div className="col-span-3 min-h-0">
                        <IncidentsList
                            incidents={incidents}
                            selectedIncidentId={selectedIncidentId ?? ''}
                            onIncidentSelect={handleIncidentSelect}
                            onCreateManual={() => {
                                setSelectedAlertData(undefined);
                                setCreateModalOpen(true);
                            }}
                            filters={incidentFilters}
                            onFilterChange={setIncidentFilters}
                        />
                    </div>

                    {/* Center Column: Airport Map */}
                    <div className="col-span-6 min-h-0">
                        <AirportMap
                            incidents={incidents}
                            selectedIncidentId={selectedIncidentId ?? ''}
                            onIncidentSelect={handleIncidentSelect}
                        />
                    </div>

                    {/* Right Column: Alerts & Details */}
                    <div className="col-span-3 flex flex-col gap-4 min-h-0 overflow-y-auto">
                        <AlertsPanel
                            alerts={alerts}
                            onCreateIncident={handleCreateIncident}
                        />
                        {selectedIncident && (
                            <IncidentDetails incident={selectedIncident} />
                        )}
                        {/* <BackendStatus /> */}
                    </div>
                </div>
            </main>

            {/* Create Incident Modal */}
            <CreateIncidentModal
                open={createModalOpen}
                onOpenChange={setCreateModalOpen}
                alertData={selectedAlertData}
            />
        </div>
    );
}
