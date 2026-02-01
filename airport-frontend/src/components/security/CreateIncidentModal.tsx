import { useState, useEffect } from 'react';
import * as DialogPrimitive from '@radix-ui/react-dialog';
import { Button } from '@/components/ui/button';
import { Label } from '@/components/ui/label';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { X, Loader2 } from 'lucide-react';
import { useCreateIncident, useTeams } from '@/hooks';
import type { IncidentType, IncidentPriority } from '@/types';

interface CreateIncidentModalProps {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    alertData?: {
        sourceSystem: string;
        detectionTime: string;
        sensorId: string;
        suggestedType?: string;
        suggestedPriority?: string;
        suggestedLocation?: string;
        sensorEventId?: number;
    };
}

// Map UI display values to backend enum values
const typeToBackend: Record<string, IncidentType> = {
    'Fire Alarm': 'FIRE_STRUCTURAL',
    'Medical Emergency': 'MEDICAL_EMERGENCY',
    'Security Breach': 'SECURITY_UNAUTHORIZED_ACCESS',
    'Unauthorized Access': 'SECURITY_UNAUTHORIZED_ACCESS',
    'Suspicious Package': 'SECURITY_SUSPICIOUS_PACKAGE',
    'Technical Failure': 'TECHNICAL_EQUIPMENT_FAILURE',
    'Other': 'OTHER_MISCELLANEOUS',
};

const priorityToBackend: Record<string, IncidentPriority> = {
    'Critical': 'CRITICAL',
    'High': 'HIGH',
    'Medium': 'NORMAL',
    'Low': 'LOW',
};

export function CreateIncidentModal({ open, onOpenChange, alertData }: CreateIncidentModalProps) {
    const createIncidentMutation = useCreateIncident();

    // Form state
    const [incidentType, setIncidentType] = useState('Fire Alarm');
    const [priority, setPriority] = useState('Critical');
    const [location, setLocation] = useState('');
    const [description, setDescription] = useState('');
    const [assignedTeam, setAssignedTeam] = useState('');
    const [descriptionError, setDescriptionError] = useState<string | null>(null);

    // Fetch teams based on selected specialization
    const { data: teams = [], isLoading: isLoadingTeams } = useTeams(typeToBackend[incidentType]);

    // Update form when alertData changes
    useEffect(() => {
        if (alertData) {
            setIncidentType(alertData.suggestedType || 'Fire Alarm');
            setPriority(alertData.suggestedPriority || 'Critical');
            setLocation(alertData.suggestedLocation || '');
            setDescription(`Alert from ${alertData.sourceSystem} (${alertData.sensorId}) detected at ${alertData.detectionTime}. Please verify the situation and update as needed.`);
            setDescriptionError(null);
        } else if (open) {
            // Reset for manual creation when opened without alertData
            setIncidentType('Fire Alarm');
            setPriority('Medium');
            setLocation('');
            setDescription('');
            setDescriptionError(null);
            setAssignedTeam('');
        }
    }, [alertData, open]);

    const validateDescription = (): boolean => {
        if (description.length < 20) {
            setDescriptionError(`Description must be at least 20 characters (currently ${description.length})`);
            return false;
        }
        setDescriptionError(null);
        return true;
    };

    const handleCreateIncident = () => {
        if (!validateDescription()) {
            return;
        }

        const requestData = {
            type: typeToBackend[incidentType] || 'OTHER',
            priority: priorityToBackend[priority] || 'NORMAL',
            locationId: 1, // Default location - would need to resolve from location name
            description,
            status: 'NEW' as const,
            assignedTeamId: assignedTeam ? parseInt(assignedTeam) : undefined,
            sensorEventId: alertData?.sensorEventId,
        };

        console.log('Creating incident with data:', requestData);
        console.log('alertData:', alertData);

        createIncidentMutation.mutate(requestData, {
            onSuccess: () => {
                onOpenChange(false);
            },
        });
    };

    const handleCreateAndAssign = () => {
        if (!assignedTeam) {
            alert('Please select a team to assign');
            return;
        }
        handleCreateIncident();
    };

    const isManual = !alertData;

    return (
        <DialogPrimitive.Root open={open} onOpenChange={onOpenChange}>
            <DialogPrimitive.Portal>
                <DialogPrimitive.Overlay className="fixed inset-0 z-50 bg-black/70 data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0" />
                <DialogPrimitive.Content
                    className="fixed top-[50%] left-[50%] z-50 w-full max-w-2xl translate-x-[-50%] translate-y-[-50%] rounded-xl bg-[#2C2C2E] shadow-2xl data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0 data-[state=closed]:zoom-out-95 data-[state=open]:zoom-in-95 duration-200"
                    style={{ fontFamily: 'Inter, Roboto, system-ui, sans-serif' }}
                >
                    {/* Modal Header */}
                    <div className="flex items-center justify-between p-6 pb-4">
                        <h2 className="text-white font-semibold flex items-center gap-2">
                            {isManual ? 'Report New Security Incident' : 'Create Incident from Alert'}
                        </h2>
                        <DialogPrimitive.Close asChild>
                            <button
                                className="text-slate-400 hover:text-white transition-colors rounded-sm focus:outline-none focus:ring-2 focus:ring-slate-400 focus:ring-offset-2 focus:ring-offset-[#2C2C2E]"
                            >
                                <X className="w-5 h-5" />
                            </button>
                        </DialogPrimitive.Close>
                    </div>

                    <div className="px-6 pb-6 space-y-6">
                        {/* Section 1: System Data (Read-Only) - Only shown if Not manual */}
                        {!isManual && (
                            <div className="space-y-3">
                                <h3 className="text-slate-400 tracking-wider text-xs">AUTOMATED ALERT DATA</h3>

                                <div className="grid grid-cols-3 gap-4">
                                    <div className="space-y-2">
                                        <Label className="text-slate-400 text-xs">Source System</Label>
                                        <div className="bg-[#1C1C1E] rounded-md px-3 py-2.5 text-slate-300 text-sm border border-slate-800">
                                            {alertData.sourceSystem}
                                        </div>
                                    </div>

                                    <div className="space-y-2">
                                        <Label className="text-slate-400 text-xs">Detection Time</Label>
                                        <div className="bg-[#1C1C1E] rounded-md px-3 py-2.5 text-slate-300 text-sm border border-slate-800">
                                            {alertData.detectionTime}
                                        </div>
                                    </div>

                                    <div className="space-y-2">
                                        <Label className="text-slate-400 text-xs">Sensor ID</Label>
                                        <div className="bg-[#1C1C1E] rounded-md px-3 py-2.5 text-slate-300 text-sm border border-slate-800">
                                            {alertData.sensorId}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}

                        {/* Section 2: Incident Details (Editable) */}
                        <div className="space-y-4">
                            <h3 className="text-slate-400 tracking-wider text-xs">VERIFY AND CATEGORIZE</h3>

                            <div className="grid grid-cols-2 gap-4">
                                <div className="space-y-2">
                                    <Label className="text-slate-300">Incident Type</Label>
                                    <Select value={incidentType} onValueChange={setIncidentType}>
                                        <SelectTrigger className="bg-[#1C1C1E] border-[#0A84FF] text-white hover:bg-[#252527]">
                                            <SelectValue />
                                        </SelectTrigger>
                                        <SelectContent className="bg-[#2C2C2E] border-slate-700">
                                            <SelectItem value="Fire Alarm" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Fire Alarm</SelectItem>
                                            <SelectItem value="Medical Emergency" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Medical Emergency</SelectItem>
                                            <SelectItem value="Security Breach" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Security Breach</SelectItem>
                                            <SelectItem value="Unauthorized Access" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Unauthorized Access</SelectItem>
                                            <SelectItem value="Suspicious Package" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Suspicious Package</SelectItem>
                                            <SelectItem value="Technical Failure" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Technical Failure</SelectItem>
                                        </SelectContent>
                                    </Select>
                                </div>

                                <div className="space-y-2">
                                    <Label className="text-slate-300">Priority</Label>
                                    <Select value={priority} onValueChange={setPriority}>
                                        <SelectTrigger className="bg-[#1C1C1E] border-[#0A84FF] text-white hover:bg-[#252527]">
                                            <SelectValue />
                                        </SelectTrigger>
                                        <SelectContent className="bg-[#2C2C2E] border-slate-700">
                                            <SelectItem value="Critical" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Critical</SelectItem>
                                            <SelectItem value="High" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">High</SelectItem>
                                            <SelectItem value="Medium" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Medium</SelectItem>
                                            <SelectItem value="Low" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Low</SelectItem>
                                        </SelectContent>
                                    </Select>
                                </div>
                            </div>

                            <div className="space-y-2">
                                <Label className="text-slate-300">Location</Label>
                                <Input
                                    value={location}
                                    onChange={(e) => setLocation(e.target.value)}
                                    className="bg-[#1C1C1E] border-[#0A84FF] text-white placeholder:text-slate-500 hover:bg-[#252527] focus-visible:ring-[#0A84FF]"
                                />
                            </div>

                            <div className="space-y-2">
                                <Label className="text-slate-300">Description <span className="text-slate-500">(min. 20 characters)</span></Label>
                                <Textarea
                                    rows={4}
                                    value={description}
                                    onChange={(e) => {
                                        setDescription(e.target.value);
                                        if (descriptionError && e.target.value.length >= 20) {
                                            setDescriptionError(null);
                                        }
                                    }}
                                    className={`bg-[#1C1C1E] text-white placeholder:text-slate-500 resize-none hover:bg-[#252527] focus-visible:ring-[#0A84FF] ${descriptionError ? 'border-red-500' : 'border-[#0A84FF]'
                                        }`}
                                />
                                {descriptionError && (
                                    <p className="text-red-400 text-xs mt-1">{descriptionError}</p>
                                )}
                            </div>
                        </div>

                        {/* Section 3: Assign Team (Optional) */}
                        <div className="space-y-3">
                            <h3 className="text-slate-400 tracking-wider text-xs">IMMEDIATE DISPATCH (OPTIONAL)</h3>

                            <div className="space-y-2">
                                <Label className="text-slate-300">Assign a Team</Label>
                                <Select value={assignedTeam} onValueChange={setAssignedTeam}>
                                    <SelectTrigger className="bg-[#1C1C1E] border-slate-700 text-slate-400 hover:bg-[#252527]">
                                        <SelectValue placeholder={isLoadingTeams ? "Loading teams..." : "Select an available team..."} />
                                    </SelectTrigger>
                                    <SelectContent className="bg-[#2C2C2E] border-slate-700">
                                        {teams.length > 0 ? (
                                            teams.map((team: any) => (
                                                <SelectItem
                                                    key={team.id}
                                                    value={team.id.toString()}
                                                    className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]"
                                                >
                                                    {team.teamName} ({team.status.toLowerCase().replace('_', ' ')})
                                                </SelectItem>
                                            ))
                                        ) : (
                                            <div className="p-2 text-xs text-slate-500">No teams available for this specialization</div>
                                        )}
                                    </SelectContent>
                                </Select>
                            </div>
                        </div>

                        {/* Action Buttons */}
                        <div className="flex items-center justify-end gap-3 pt-2">
                            <Button
                                onClick={() => onOpenChange(false)}
                                variant="outline"
                                className="border-slate-600 text-slate-300 hover:bg-slate-800 hover:text-white"
                                disabled={createIncidentMutation.isPending}
                            >
                                Cancel
                            </Button>
                            <Button
                                onClick={handleCreateAndAssign}
                                variant="outline"
                                className="border-[#0A84FF] text-[#0A84FF] hover:bg-[#0A84FF]/10"
                                disabled={createIncidentMutation.isPending}
                            >
                                Create & Assign
                            </Button>
                            <Button
                                onClick={handleCreateIncident}
                                className="bg-[#FF3B30] text-white hover:bg-[#FF3B30]/90"
                                disabled={createIncidentMutation.isPending}
                            >
                                {createIncidentMutation.isPending ? (
                                    <>
                                        <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                                        Creating...
                                    </>
                                ) : (
                                    'Create Incident'
                                )}
                            </Button>
                        </div>
                    </div>
                </DialogPrimitive.Content>
            </DialogPrimitive.Portal>
        </DialogPrimitive.Root>
    );
}
