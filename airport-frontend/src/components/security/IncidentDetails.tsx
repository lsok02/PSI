import { Clock, Users, MapPin, AlertTriangle, ArrowUpCircle } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Separator } from '@/components/ui/separator';
import type { Incident } from '@/types';

interface IncidentDetailsProps {
    incident: Incident;
}

export function IncidentDetails({ incident }: IncidentDetailsProps) {
    const getPriorityColor = (priority: string) => {
        switch (priority) {
            case 'Critical':
                return 'bg-red-950 text-red-300 border-red-800';
            case 'High':
                return 'bg-orange-950 text-orange-300 border-orange-800';
            case 'Medium':
                return 'bg-yellow-950 text-yellow-300 border-yellow-800';
            default:
                return 'bg-blue-950 text-blue-300 border-blue-800';
        }
    };

    return (
        <Card className="bg-slate-900 border-slate-800 shrink-0">
            <div className="p-4 border-b border-slate-800">
                <h2 className="text-slate-100 mb-1">Incident Details: {incident.id}</h2>
                <p className="text-slate-400 text-xs">{incident.type}</p>
            </div>

            <div className="p-4 space-y-4">
                {/* Key Information */}
                <div className="space-y-3">
                    <div className="flex items-center justify-between">
                        <span className="text-slate-400 text-xs">Priority</span>
                        <Badge variant="outline" className={getPriorityColor(incident.priority)}>
                            {incident.priority}
                        </Badge>
                    </div>

                    <div className="flex items-center justify-between">
                        <span className="text-slate-400 text-xs">Status</span>
                        <Badge variant="outline" className="bg-blue-950 text-blue-300 border-blue-800">
                            {incident.status}
                        </Badge>
                    </div>

                    <Separator className="bg-slate-800" />

                    <div className="flex items-start gap-2">
                        <MapPin className="w-4 h-4 text-blue-400 mt-0.5" />
                        <div className="flex-1">
                            <p className="text-slate-400 text-xs">Location</p>
                            <p className="text-slate-200">{incident.location}</p>
                        </div>
                    </div>

                    <div className="flex items-start gap-2">
                        <Users className="w-4 h-4 text-blue-400 mt-0.5" />
                        <div className="flex-1">
                            <p className="text-slate-400 text-xs">Assigned Team</p>
                            <p className="text-slate-200">{incident.assignedTeam}</p>
                        </div>
                    </div>

                    <div className="flex items-start gap-2">
                        <AlertTriangle className="w-4 h-4 text-blue-400 mt-0.5" />
                        <div className="flex-1">
                            <p className="text-slate-400 text-xs">Description</p>
                            <p className="text-slate-200">{incident.description}</p>
                        </div>
                    </div>
                </div>

                <Separator className="bg-slate-800" />

                {/* Timeline */}
                <div>
                    <div className="flex items-center gap-2 mb-3">
                        <Clock className="w-4 h-4 text-blue-400" />
                        <h3 className="text-slate-200">Timeline</h3>
                    </div>
                    <div className="space-y-2 ml-6">
                        {incident.timeline.map((entry, index) => (
                            <div key={index} className="relative pl-4 pb-2 border-l border-slate-700 last:border-0">
                                <div className="absolute left-0 top-1 w-2 h-2 rounded-full bg-blue-500 -translate-x-[5px]"></div>
                                <p className="text-slate-400 text-xs">{entry.time}</p>
                                <p className="text-slate-200 text-xs mt-0.5">{entry.action}</p>
                            </div>
                        ))}
                    </div>
                </div>

                <Separator className="bg-slate-800" />

                {/* Action Buttons */}
                <div className="space-y-2">
                    <Button className="w-full bg-blue-600 hover:bg-blue-700 text-white">
                        <Users className="w-4 h-4 mr-2" />
                        Assign Team
                    </Button>
                    <Button variant="outline" className="w-full border-orange-700 text-orange-400 hover:bg-orange-950">
                        <ArrowUpCircle className="w-4 h-4 mr-2" />
                        Escalate
                    </Button>
                </div>
            </div>
        </Card>
    );
}
