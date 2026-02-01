import { useState } from 'react';
import {
    IncidentsList,
    AirportMap,
    AlertsPanel,
    IncidentDetails,
    CreateIncidentModal
} from '@/components/security';
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

    const selectedIncident = incidents.find(inc => inc.id === selectedIncidentId);

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
        <div className="min-h-screen bg-slate-950 text-slate-100 p-4">
            <div className="mb-6">
                <h1 className="text-slate-100 mb-1">Airport Security Command Center</h1>
                <p className="text-slate-400">Real-time Incident Management Dashboard</p>
            </div>

            <div className="grid grid-cols-12 gap-4 h-[calc(100vh-140px)]">
                {/* Left Column: Active Incidents */}
                <div className="col-span-3">
                    <IncidentsList
                        incidents={incidents}
                        selectedIncidentId={selectedIncidentId}
                        onIncidentSelect={handleIncidentSelect}
                    />
                </div>

                {/* Center Column: Airport Map */}
                <div className="col-span-6">
                    <AirportMap
                        incidents={incidents}
                        responseTeams={responseTeams}
                        selectedIncidentId={selectedIncidentId}
                        onIncidentSelect={handleIncidentSelect}
                    />
                </div>

                {/* Right Column: Alerts & Details */}
                <div className="col-span-3 flex flex-col gap-4">
                    <AlertsPanel
                        alerts={alerts}
                        onCreateIncident={handleCreateIncident}
                        onDismissAlert={handleDismissAlert}
                    />
                    {selectedIncident && (
                        <IncidentDetails incident={selectedIncident} />
                    )}
                </div>
            </div>

            {/* Create Incident Modal */}
            <CreateIncidentModal
                open={createModalOpen}
                onOpenChange={setCreateModalOpen}
            />
        </div>
    );
}
