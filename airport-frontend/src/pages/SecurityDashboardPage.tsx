import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Shield, Plane, LogOut, User } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useAuth } from '@/context';
import {
    IncidentsList,
    AirportMap,
    AlertsPanel,
    IncidentDetails,
    CreateIncidentModal
} from '@/components/security';
import { BackendStatus } from '@/components/BackendStatus';
import type { Incident, Alert, ResponseTeam } from '@/types';

// Mock data
const mockIncidents: Incident[] = [
    {
        id: 'INC-0125',
        type: 'Medical Emergency',
        priority: 'Critical',
        location: 'Gate G15',
        status: 'In Progress',
        assignedTeam: 'Medic-1',
        coordinates: { x: 65, y: 35 },
        timestamp: '14:32',
        description: 'Passenger collapsed at gate, requires immediate medical attention',
        timeline: [
            { time: '14:32', action: 'Incident reported by gate staff' },
            { time: '14:33', action: 'Medic-1 dispatched' },
            { time: '14:35', action: 'Medic-1 arrived on scene' },
            { time: '14:37', action: 'Patient stabilized, preparing for transport' }
        ]
    },
    {
        id: 'INC-0126',
        type: 'Fire Alarm',
        priority: 'Critical',
        location: 'Terminal B - Baggage',
        status: 'In Progress',
        assignedTeam: 'Fire-2',
        coordinates: { x: 30, y: 65 },
        timestamp: '14:28',
        description: 'Fire alarm triggered in baggage handling area',
        timeline: [
            { time: '14:28', action: 'Alarm activated' },
            { time: '14:29', action: 'Fire-2 dispatched' },
            { time: '14:31', action: 'Fire-2 on scene, investigating' }
        ]
    },
    {
        id: 'INC-0127',
        type: 'Security Breach',
        priority: 'High',
        location: 'Checkpoint C',
        status: 'Under Investigation',
        assignedTeam: 'Security-3',
        coordinates: { x: 45, y: 50 },
        timestamp: '14:15',
        description: 'Unattended bag detected at security checkpoint',
        timeline: [
            { time: '14:15', action: 'Bag reported by TSA officer' },
            { time: '14:16', action: 'Area cordoned off' },
            { time: '14:18', action: 'Security-3 dispatched' },
            { time: '14:20', action: 'Bomb squad notified' }
        ]
    },
    {
        id: 'INC-0128',
        type: 'Unauthorized Access',
        priority: 'High',
        location: 'Maintenance Bay 7',
        status: 'In Progress',
        assignedTeam: 'Security-1',
        coordinates: { x: 80, y: 70 },
        timestamp: '14:10',
        description: 'Unauthorized personnel detected in restricted area',
        timeline: [
            { time: '14:10', action: 'Access control system triggered' },
            { time: '14:11', action: 'Security-1 dispatched' },
            { time: '14:14', action: 'Personnel detained for questioning' }
        ]
    },
    {
        id: 'INC-0129',
        type: 'Medical Emergency',
        priority: 'Medium',
        location: 'Food Court A',
        status: 'Resolved',
        assignedTeam: 'Medic-2',
        coordinates: { x: 50, y: 30 },
        timestamp: '13:55',
        description: 'Minor injury requiring first aid',
        timeline: [
            { time: '13:55', action: 'Incident reported' },
            { time: '13:57', action: 'Medic-2 dispatched' },
            { time: '14:00', action: 'First aid administered' },
            { time: '14:05', action: 'Incident resolved' }
        ]
    }
];

const mockAlerts: Alert[] = [
    {
        id: 'ALT-001',
        description: 'Unauthorized access detected at Technical Zone T-04',
        timestamp: '14:42'
    },
    {
        id: 'ALT-002',
        description: 'Suspicious package reported near Gate D12',
        timestamp: '14:40'
    }
];

const mockResponseTeams: ResponseTeam[] = [
    { id: 'team-1', name: 'Medic-1', type: 'medical', coordinates: { x: 65, y: 35 } },
    { id: 'team-2', name: 'Fire-2', type: 'fire', coordinates: { x: 30, y: 65 } },
    { id: 'team-3', name: 'Security-3', type: 'security', coordinates: { x: 45, y: 50 } },
    { id: 'team-4', name: 'Security-1', type: 'security', coordinates: { x: 80, y: 70 } }
];

export function SecurityDashboardPage() {
    const [selectedIncidentId, setSelectedIncidentId] = useState<string>('INC-0125');
    const [createModalOpen, setCreateModalOpen] = useState(false);
    const [incidents] = useState(mockIncidents);
    const [alerts] = useState(mockAlerts);
    const [responseTeams] = useState(mockResponseTeams);
    const { logout, username } = useAuth();
    const navigate = useNavigate();

    const selectedIncident = incidents.find(inc => inc.id === selectedIncidentId);

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const handleIncidentSelect = (incidentId: string) => {
        setSelectedIncidentId(incidentId);
    };

    const handleCreateIncident = (alertId: string) => {
        console.log('Creating incident from alert:', alertId);
        setCreateModalOpen(true);
    };

    const handleDismissAlert = (alertId: string) => {
        console.log('Dismissing alert:', alertId);
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
                            selectedIncidentId={selectedIncidentId}
                            onIncidentSelect={handleIncidentSelect}
                        />
                    </div>

                    {/* Center Column: Airport Map */}
                    <div className="col-span-6 min-h-0">
                        <AirportMap
                            incidents={incidents}
                            responseTeams={responseTeams}
                            selectedIncidentId={selectedIncidentId}
                            onIncidentSelect={handleIncidentSelect}
                        />
                    </div>

                    {/* Right Column: Alerts & Details */}
                    <div className="col-span-3 flex flex-col gap-4 min-h-0 overflow-y-auto">
                        <AlertsPanel
                            alerts={alerts}
                            onCreateIncident={handleCreateIncident}
                            onDismissAlert={handleDismissAlert}
                        />
                        {selectedIncident && (
                            <IncidentDetails incident={selectedIncident} />
                        )}
                        <BackendStatus />
                    </div>
                </div>
            </main>

            {/* Create Incident Modal */}
            <CreateIncidentModal
                open={createModalOpen}
                onOpenChange={setCreateModalOpen}
            />
        </div>
    );
}
