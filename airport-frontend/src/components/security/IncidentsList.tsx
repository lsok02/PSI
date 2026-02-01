import { Flame, AlertTriangle, ShieldAlert, Activity } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { ScrollArea } from '@/components/ui/scroll-area';
import type { Incident } from '@/types';

interface IncidentsListProps {
    incidents: Incident[];
    selectedIncidentId: string;
    onIncidentSelect: (incidentId: string) => void;
}

export function IncidentsList({ incidents, selectedIncidentId, onIncidentSelect }: IncidentsListProps) {
    const getPriorityIcon = (priority: string) => {
        switch (priority) {
            case 'Critical':
                return <Flame className="w-4 h-4 text-red-500" />;
            case 'High':
                return <AlertTriangle className="w-4 h-4 text-orange-500" />;
            case 'Medium':
                return <ShieldAlert className="w-4 h-4 text-yellow-500" />;
            default:
                return <Activity className="w-4 h-4 text-blue-500" />;
        }
    };

    const getPriorityColor = (priority: string) => {
        switch (priority) {
            case 'Critical':
                return 'border-l-red-500';
            case 'High':
                return 'border-l-orange-500';
            case 'Medium':
                return 'border-l-yellow-500';
            default:
                return 'border-l-blue-500';
        }
    };

    const getStatusColor = (status: string) => {
        if (status === 'Resolved') return 'bg-green-950 text-green-300 border-green-800';
        if (status === 'In Progress') return 'bg-blue-950 text-blue-300 border-blue-800';
        return 'bg-slate-800 text-slate-300 border-slate-700';
    };

    return (
        <Card className="bg-slate-900 border-slate-800 h-full flex flex-col">
            <div className="p-4 border-b border-slate-800">
                <h2 className="text-slate-100">Active Incidents ({incidents.length})</h2>
            </div>
            <ScrollArea className="flex-1">
                <div className="p-2 space-y-2">
                    {incidents.map((incident) => (
                        <div
                            key={incident.id}
                            onClick={() => onIncidentSelect(incident.id)}
                            className={`p-3 bg-slate-950 border border-slate-800 border-l-4 ${getPriorityColor(incident.priority)} rounded-md cursor-pointer transition-all hover:bg-slate-900 ${selectedIncidentId === incident.id ? 'ring-2 ring-blue-500' : ''
                                }`}
                        >
                            <div className="flex items-start justify-between mb-2">
                                <div className="flex items-center gap-2">
                                    {getPriorityIcon(incident.priority)}
                                    <span className="text-slate-300">{incident.id}</span>
                                </div>
                                <Badge variant="outline" className={`${getStatusColor(incident.status)} text-xs`}>
                                    {incident.status}
                                </Badge>
                            </div>

                            <div className="space-y-1">
                                <p className="text-slate-100">{incident.type}</p>
                                <div className="flex items-center gap-2 text-slate-400">
                                    <span className="text-xs">📍 {incident.location}</span>
                                </div>
                                <div className="flex items-center gap-2 text-slate-400">
                                    <span className="text-xs">👥 {incident.assignedTeam}</span>
                                </div>
                                <div className="text-xs text-slate-500 mt-2">
                                    {incident.timestamp}
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </ScrollArea>
        </Card>
    );
}
