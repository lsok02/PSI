import * as DialogPrimitive from '@radix-ui/react-dialog';
import { Button } from '@/components/ui/button';
import { Label } from '@/components/ui/label';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { X } from 'lucide-react';

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
    };
}

export function CreateIncidentModal({ open, onOpenChange, alertData }: CreateIncidentModalProps) {
    const handleCreateIncident = () => {
        console.log('Creating incident...');
        onOpenChange(false);
    };

    const handleCreateAndAssign = () => {
        console.log('Creating incident and assigning team...');
        onOpenChange(false);
    };

    const defaultAlertData = {
        sourceSystem: 'Fire Detection System (PPOŻ)',
        detectionTime: '2025-11-02 14:45:10',
        sensorId: 'SD-T1-C-112',
        suggestedType: 'Fire Alarm',
        suggestedPriority: 'Critical',
        suggestedLocation: 'Main Departures Hall, Zone C'
    };

    const data = alertData || defaultAlertData;

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
                        <h2 className="text-white">Create New Incident from Alert</h2>
                        <DialogPrimitive.Close asChild>
                            <button
                                className="text-slate-400 hover:text-white transition-colors rounded-sm focus:outline-none focus:ring-2 focus:ring-slate-400 focus:ring-offset-2 focus:ring-offset-[#2C2C2E]"
                            >
                                <X className="w-5 h-5" />
                            </button>
                        </DialogPrimitive.Close>
                    </div>

                    <div className="px-6 pb-6 space-y-6">
                        {/* Section 1: System Data (Read-Only) */}
                        <div className="space-y-3">
                            <h3 className="text-slate-400 tracking-wider text-xs">AUTOMATED ALERT DATA</h3>

                            <div className="grid grid-cols-3 gap-4">
                                <div className="space-y-2">
                                    <Label className="text-slate-400 text-xs">Source System</Label>
                                    <div className="bg-[#1C1C1E] rounded-md px-3 py-2.5 text-slate-300 text-sm border border-slate-800">
                                        {data.sourceSystem}
                                    </div>
                                </div>

                                <div className="space-y-2">
                                    <Label className="text-slate-400 text-xs">Detection Time</Label>
                                    <div className="bg-[#1C1C1E] rounded-md px-3 py-2.5 text-slate-300 text-sm border border-slate-800">
                                        {data.detectionTime}
                                    </div>
                                </div>

                                <div className="space-y-2">
                                    <Label className="text-slate-400 text-xs">Sensor ID</Label>
                                    <div className="bg-[#1C1C1E] rounded-md px-3 py-2.5 text-slate-300 text-sm border border-slate-800">
                                        {data.sensorId}
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* Section 2: Incident Details (Editable) */}
                        <div className="space-y-4">
                            <h3 className="text-slate-400 tracking-wider text-xs">VERIFY AND CATEGORIZE</h3>

                            <div className="grid grid-cols-2 gap-4">
                                <div className="space-y-2">
                                    <Label className="text-slate-300">Incident Type</Label>
                                    <Select defaultValue={data.suggestedType}>
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
                                    <Select defaultValue={data.suggestedPriority}>
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
                                    defaultValue={data.suggestedLocation}
                                    className="bg-[#1C1C1E] border-[#0A84FF] text-white placeholder:text-slate-500 hover:bg-[#252527] focus-visible:ring-[#0A84FF]"
                                />
                            </div>

                            <div className="space-y-2">
                                <Label className="text-slate-300">Description</Label>
                                <Textarea
                                    rows={4}
                                    defaultValue="Automatically generated incident based on a fire alarm alert. Please verify the situation and update as necessary. Initial sensor reading indicates potential fire hazard in the specified zone."
                                    className="bg-[#1C1C1E] border-[#0A84FF] text-white placeholder:text-slate-500 resize-none hover:bg-[#252527] focus-visible:ring-[#0A84FF]"
                                />
                            </div>
                        </div>

                        {/* Section 3: Assign Team (Optional) */}
                        <div className="space-y-3">
                            <h3 className="text-slate-400 tracking-wider text-xs">IMMEDIATE DISPATCH (OPTIONAL)</h3>

                            <div className="space-y-2">
                                <Label className="text-slate-300">Assign a Team</Label>
                                <Select>
                                    <SelectTrigger className="bg-[#1C1C1E] border-slate-700 text-slate-400 hover:bg-[#252527]">
                                        <SelectValue placeholder="Select an available team..." />
                                    </SelectTrigger>
                                    <SelectContent className="bg-[#2C2C2E] border-slate-700">
                                        <SelectItem value="fire-1" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Fire-1 (Available)</SelectItem>
                                        <SelectItem value="fire-2" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Fire-2 (On Scene - INC-0126)</SelectItem>
                                        <SelectItem value="medic-1" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Medic-1 (On Scene - INC-0125)</SelectItem>
                                        <SelectItem value="medic-2" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Medic-2 (Available)</SelectItem>
                                        <SelectItem value="security-1" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Security-1 (On Scene - INC-0128)</SelectItem>
                                        <SelectItem value="security-2" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Security-2 (Available)</SelectItem>
                                        <SelectItem value="security-3" className="text-white hover:bg-[#3C3C3E] focus:bg-[#3C3C3E]">Security-3 (On Scene - INC-0127)</SelectItem>
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
                            >
                                Cancel
                            </Button>
                            <Button
                                onClick={handleCreateAndAssign}
                                variant="outline"
                                className="border-[#0A84FF] text-[#0A84FF] hover:bg-[#0A84FF]/10"
                            >
                                Create & Assign
                            </Button>
                            <Button
                                onClick={handleCreateIncident}
                                className="bg-[#FF3B30] text-white hover:bg-[#FF3B30]/90"
                            >
                                Create Incident
                            </Button>
                        </div>
                    </div>
                </DialogPrimitive.Content>
            </DialogPrimitive.Portal>
        </DialogPrimitive.Root>
    );
}
