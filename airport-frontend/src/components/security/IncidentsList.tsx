import { Flame, AlertTriangle, ShieldAlert, Activity, Plus } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { XCircle } from 'lucide-react';
import type { Incident } from '@/types';

interface Filters {
    priority: string;
    status: string;
}

interface IncidentsListProps {
    incidents: Incident[];
    selectedIncidentId: string;
    onIncidentSelect: (incidentId: string) => void;
    onCreateManual?: () => void;
    filters: Filters;
    onFilterChange: (filters: Filters) => void;
}

export function IncidentsList({
    incidents,
    selectedIncidentId,
    onIncidentSelect,
    onCreateManual,
    filters,
    onFilterChange
}: IncidentsListProps) {
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
            <div className="p-4 border-b border-slate-800 space-y-4">
                <div className="flex items-center justify-between">
                    <h2 className="text-slate-100 font-medium">Active Incidents ({incidents.length})</h2>
                    <div className="flex gap-2">
                        {(filters.priority !== 'all' || filters.status !== 'all') && (
                            <Button
                                onClick={() => onFilterChange({ priority: 'all', status: 'all' })}
                                size="sm"
                                variant="ghost"
                                className="h-8 px-2 text-slate-400 hover:text-slate-200"
                            >
                                <XCircle className="w-4 h-4" />
                            </Button>
                        )}
                        {onCreateManual && (
                            <Button
                                onClick={onCreateManual}
                                size="sm"
                                variant="ghost"
                                className="h-8 px-2 text-blue-400 hover:text-blue-300 hover:bg-blue-900/20 gap-1 border border-blue-900/50"
                            >
                                <Plus className="w-4 h-4" />
                                <span className="text-xs">Create</span>
                            </Button>
                        )}
                    </div>
                </div>

                <div className="grid grid-cols-2 gap-2">
                    <div className="space-y-1">
                        <span className="text-[10px] text-slate-500 uppercase tracking-wider ml-1">Priority</span>
                        <Select
                            value={filters.priority}
                            onValueChange={(val) => onFilterChange({ ...filters, priority: val })}
                        >
                            <SelectTrigger className="h-8 bg-slate-950 border-slate-800 text-xs text-slate-300 focus:ring-0 focus:ring-offset-0">
                                <SelectValue placeholder="Priority" />
                            </SelectTrigger>
                            <SelectContent className="bg-slate-900 border-slate-800 text-slate-200">
                                <SelectItem value="all">All Priorities</SelectItem>
                                <SelectItem value="CRITICAL">Critical</SelectItem>
                                <SelectItem value="HIGH">High</SelectItem>
                                <SelectItem value="NORMAL">Medium</SelectItem>
                                <SelectItem value="LOW">Low</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>

                    <div className="space-y-1">
                        <span className="text-[10px] text-slate-500 uppercase tracking-wider ml-1">Status</span>
                        <Select
                            value={filters.status}
                            onValueChange={(val) => onFilterChange({ ...filters, status: val })}
                        >
                            <SelectTrigger className="h-8 bg-slate-950 border-slate-800 text-xs text-slate-300 focus:ring-0 focus:ring-offset-0">
                                <SelectValue placeholder="Status" />
                            </SelectTrigger>
                            <SelectContent className="bg-slate-900 border-slate-800 text-slate-200">
                                <SelectItem value="all">All Statuses</SelectItem>
                                <SelectItem value="NEW">New</SelectItem>
                                <SelectItem value="ASSIGNED">Assigned</SelectItem>
                                <SelectItem value="IN_PROGRESS">In Progress</SelectItem>
                                <SelectItem value="RESOLVED">Resolved</SelectItem>
                                <SelectItem value="CLOSED">Closed</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>
                </div>
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
                                {incident.reportSource && (
                                    <div className="flex items-center gap-2 text-slate-400">
                                        <span className="text-xs">Source: {incident.reportSource.replace(/_/g, ' ')}</span>
                                    </div>
                                )}
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
