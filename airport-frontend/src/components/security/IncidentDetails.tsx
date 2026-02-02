import { Clock, Users, MapPin, AlertTriangle, ArrowUpCircle, CheckCircle, Loader2, XCircle, CheckCheck } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Label } from '@/components/ui/label';
import { Badge } from '@/components/ui/badge';
import { Separator } from '@/components/ui/separator';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { useState, useEffect } from 'react';
import { useEscalateIncident, useTeams, useAssignTeam, useUpdateIncidentStatus } from '@/hooks/useIncidents';
import type { Incident, IncidentType } from '@/types';

// Map UI display values to backend enum values for team specialization
const typeToBackend: Record<string, IncidentType> = {
    'Fire Alarm': 'FIRE_STRUCTURAL',
    'Medical Emergency': 'MEDICAL_EMERGENCY',
    'Security Threat': 'SECURITY_UNAUTHORIZED_ACCESS',
    'Unauthorized Access': 'SECURITY_UNAUTHORIZED_ACCESS',
    'Suspicious Package': 'SECURITY_SUSPICIOUS_PACKAGE',
    'Technical Issue': 'TECHNICAL_EQUIPMENT_FAILURE',
    'Equipment Failure': 'TECHNICAL_EQUIPMENT_FAILURE',
    'First Aid': 'MEDICAL_FIRST_AID',
    'Other': 'OTHER_MISCELLANEOUS',
};

interface IncidentDetailsProps {
    incident: Incident;
}

export function IncidentDetails({ incident }: IncidentDetailsProps) {
    const [isConfirming, setIsConfirming] = useState(false);
    const [selectedTeamId, setSelectedTeamId] = useState<string>('');
    const [confirmingAction, setConfirmingAction] = useState<'resolve' | 'close' | null>(null);

    const escalateMutation = useEscalateIncident();
    const assignTeamMutation = useAssignTeam();
    const updateStatusMutation = useUpdateIncidentStatus();

    // Fetch teams based on incident type
    const backendType = typeToBackend[incident.type] || 'OTHER_MISCELLANEOUS';
    const { data: teams = [], isLoading: isLoadingTeams } = useTeams(backendType);

    // Reset state when incident changes
    useEffect(() => {
        setIsConfirming(false);
        setSelectedTeamId('');
    }, [incident.id]);

    const handleEscalate = () => {
        if (!isConfirming) {
            setIsConfirming(true);
            setTimeout(() => setIsConfirming(false), 3000);
            return;
        }

        const numericId = parseInt(incident.id.replace('INC-', ''), 10);
        escalateMutation.mutate(numericId, {
            onSuccess: () => {
                setIsConfirming(false);
            }
        });
    };

    const handleAssignTeam = () => {
        if (!selectedTeamId) return;

        const numericId = parseInt(incident.id.replace('INC-', ''), 10);
        assignTeamMutation.mutate({
            incidentId: numericId,
            teamId: parseInt(selectedTeamId)
        }, {
            onSuccess: () => {
                setSelectedTeamId('');
            }
        });
    };

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

    const isAssigned = incident.assignedTeam !== 'Unassigned';
    const isClosed = incident.status === 'Closed' || incident.status === 'CLOSED';
    const isResolved = incident.status === 'Resolved' || incident.status === 'RESOLVED';
    const canClose = !isClosed && !isResolved;

    const handleResolve = () => {
        if (confirmingAction !== 'resolve') {
            setConfirmingAction('resolve');
            setTimeout(() => setConfirmingAction(null), 3000);
            return;
        }
        const numericId = parseInt(incident.id.replace('INC-', ''), 10);
        updateStatusMutation.mutate({
            incidentId: numericId,
            status: 'RESOLVED',
            notes: 'Incident resolved - team released'
        }, {
            onSuccess: () => setConfirmingAction(null)
        });
    };

    const handleClose = () => {
        if (confirmingAction !== 'close') {
            setConfirmingAction('close');
            setTimeout(() => setConfirmingAction(null), 3000);
            return;
        }
        const numericId = parseInt(incident.id.replace('INC-', ''), 10);
        updateStatusMutation.mutate({
            incidentId: numericId,
            status: 'CLOSED',
            notes: 'Incident closed - team released'
        }, {
            onSuccess: () => setConfirmingAction(null)
        });
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
                            <p className={`font-medium ${isAssigned ? 'text-green-400' : 'text-slate-500 italic'}`}>
                                {incident.assignedTeam}
                            </p>
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
                <div className="space-y-3">
                    {!isAssigned && (
                        <div className="space-y-2">
                            <Label className="text-slate-400 text-[10px] uppercase tracking-wider">Direct Dispatch</Label>
                            <Select value={selectedTeamId} onValueChange={setSelectedTeamId}>
                                <SelectTrigger className="bg-slate-950 border-slate-700 text-slate-300">
                                    <SelectValue placeholder={isLoadingTeams ? "Loading teams..." : "Choose team to dispatch..."} />
                                </SelectTrigger>
                                <SelectContent className="bg-slate-900 border-slate-700">
                                    {teams.length > 0 ? (
                                        teams.map((team: any) => (
                                            <SelectItem
                                                key={team.id}
                                                value={team.id.toString()}
                                                className="text-slate-200 focus:bg-slate-800 focus:text-white"
                                            >
                                                {team.teamName} ({team.status.toLowerCase().replace('_', ' ')})
                                            </SelectItem>
                                        ))
                                    ) : (
                                        <div className="p-2 text-xs text-slate-500">No teams available for this type</div>
                                    )}
                                </SelectContent>
                            </Select>
                        </div>
                    )}

                    <Button
                        className={`w-full transition-all duration-200 ${isAssigned
                            ? 'bg-slate-800 text-slate-500 border-slate-700 cursor-not-allowed'
                            : 'bg-blue-600 hover:bg-blue-700 text-white'
                            }`}
                        disabled={isAssigned || !selectedTeamId || assignTeamMutation.isPending}
                        onClick={handleAssignTeam}
                    >
                        {assignTeamMutation.isPending ? (
                            <>
                                <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                                Dispatching...
                            </>
                        ) : isAssigned ? (
                            <>
                                <CheckCircle className="w-4 h-4 mr-2" />
                                Team Assigned
                            </>
                        ) : (
                            <>
                                <Users className="w-4 h-4 mr-2" />
                                {selectedTeamId ? 'Confirm Assignment' : 'Assign Team'}
                            </>
                        )}
                    </Button>
                    <Button
                        variant={isConfirming ? "destructive" : "outline"}
                        className={`w-full border-orange-700 text-orange-400 hover:bg-orange-950 transition-all duration-200 ${isConfirming ? "bg-red-900/50 border-red-500 text-red-200 hover:bg-red-900" : ""
                            }`}
                        onClick={handleEscalate}
                        disabled={incident.priority === 'Critical' || escalateMutation.isPending}
                    >
                        {escalateMutation.isPending ? (
                            <span className="flex items-center">
                                <Clock className="w-4 h-4 mr-2 animate-spin" />
                                Escalating...
                            </span>
                        ) : isConfirming ? (
                            <span className="flex items-center">
                                <CheckCircle className="w-4 h-4 mr-2" />
                                Click to Confirm
                            </span>
                        ) : (
                            <span className="flex items-center">
                                <ArrowUpCircle className="w-4 h-4 mr-2" />
                                {incident.priority === 'Critical' ? 'Max Priority' : 'Escalate'}
                            </span>
                        )}
                    </Button>
                    {isConfirming && (
                        <Button
                            variant="ghost"
                            size="sm"
                            className="w-full h-6 text-[10px] text-slate-500 hover:text-slate-300"
                            onClick={() => setIsConfirming(false)}
                        >
                            Cancel
                        </Button>
                    )}

                    {/* Close/Resolve Section */}
                    {canClose && (
                        <>
                            <Separator className="bg-slate-800 my-2" />
                            <Label className="text-slate-500 text-[10px] uppercase tracking-wider">Complete Incident</Label>
                            <div className="grid grid-cols-2 gap-2">
                                <Button
                                    variant="outline"
                                    size="sm"
                                    className={`${confirmingAction === 'resolve'
                                        ? 'border-green-500 bg-green-950/50 text-green-300'
                                        : 'border-green-700 text-green-400 hover:bg-green-950'}`}
                                    onClick={handleResolve}
                                    disabled={updateStatusMutation.isPending}
                                >
                                    {updateStatusMutation.isPending && confirmingAction === 'resolve' ? (
                                        <Loader2 className="w-3 h-3 animate-spin" />
                                    ) : confirmingAction === 'resolve' ? (
                                        <>
                                            <CheckCheck className="w-3 h-3 mr-1" />
                                            Confirm?
                                        </>
                                    ) : (
                                        <>
                                            <CheckCheck className="w-3 h-3 mr-1" />
                                            Resolve
                                        </>
                                    )}
                                </Button>
                                <Button
                                    variant="outline"
                                    size="sm"
                                    className={`${confirmingAction === 'close'
                                        ? 'border-red-500 bg-red-950/50 text-red-300'
                                        : 'border-slate-600 text-slate-400 hover:bg-slate-800'}`}
                                    onClick={handleClose}
                                    disabled={updateStatusMutation.isPending}
                                >
                                    {updateStatusMutation.isPending && confirmingAction === 'close' ? (
                                        <Loader2 className="w-3 h-3 animate-spin" />
                                    ) : confirmingAction === 'close' ? (
                                        <>
                                            <XCircle className="w-3 h-3 mr-1" />
                                            Confirm?
                                        </>
                                    ) : (
                                        <>
                                            <XCircle className="w-3 h-3 mr-1" />
                                            Close
                                        </>
                                    )}
                                </Button>
                            </div>
                        </>
                    )}
                </div>
            </div>
        </Card>
    );
}
