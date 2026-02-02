import { AlertCircle } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import type { Alert } from '@/types';

interface AlertsPanelProps {
    alerts: Alert[];
    onCreateIncident: (alertId: string) => void;
}

export function AlertsPanel({ alerts, onCreateIncident }: AlertsPanelProps) {
    return (
        <Card className="bg-slate-900 border-slate-800 shrink-0">
            <div className="p-4 border-b border-red-900 bg-red-950/20">
                <div className="flex items-center gap-2">
                    <AlertCircle className="w-5 h-5 text-red-500" />
                    <h2 className="text-red-500">New Alerts ({alerts.length})</h2>
                </div>
            </div>

            <div className="p-3 space-y-3">
                {alerts.map((alert) => (
                    <div
                        key={alert.id}
                        className="p-3 bg-red-950/30 border border-red-900/50 rounded-md"
                    >
                        <div className="flex items-start gap-2 mb-3">
                            <AlertCircle className="w-4 h-4 text-red-400 mt-0.5 flex-shrink-0" />
                            <div className="flex-1">
                                <p className="text-slate-200">{alert.description}</p>
                                <p className="text-xs text-slate-500 mt-1">{alert.timestamp}</p>
                            </div>
                        </div>

                        <Button
                            onClick={() => onCreateIncident(alert.id)}
                            size="sm"
                            className="w-full bg-red-600 hover:bg-red-700 text-white"
                        >
                            Create Incident
                        </Button>
                    </div>
                ))}
            </div>
        </Card>
    );
}
