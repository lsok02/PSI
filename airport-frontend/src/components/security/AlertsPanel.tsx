import { AlertCircle } from 'lucide-react';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import type { Alert } from '@/types';

interface AlertsPanelProps {
    alerts: Alert[];
    onCreateIncident: (alertId: string) => void;
    onDismissAlert: (alertId: string) => void;
}

export function AlertsPanel({ alerts, onCreateIncident, onDismissAlert }: AlertsPanelProps) {
    return (
        <Card className="bg-slate-900 border-slate-800">
            <div className="p-4 border-b border-red-900 bg-red-950/20">
                <div className="flex items-center gap-2">
                    <AlertCircle className="w-5 h-5 text-red-500" />
                    <h2 className="text-red-500">New Alerts ({alerts.length})</h2>
                </div>
            </div>

            <div className="p-3 space-y-3 max-h-64 overflow-y-auto">
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

                        <div className="flex gap-2">
                            <Button
                                onClick={() => onCreateIncident(alert.id)}
                                size="sm"
                                className="flex-1 bg-red-600 hover:bg-red-700 text-white"
                            >
                                Create Incident
                            </Button>
                            <Button
                                onClick={() => onDismissAlert(alert.id)}
                                size="sm"
                                variant="outline"
                                className="flex-1 border-slate-700 text-slate-300 hover:bg-slate-800"
                            >
                                Dismiss
                            </Button>
                        </div>
                    </div>
                ))}
            </div>
        </Card>
    );
}
