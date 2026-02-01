import { useState, useEffect } from 'react';
import { flightApi } from '@/api';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { RefreshCw, CheckCircle, XCircle, Loader2 } from 'lucide-react';

type ConnectionStatus = 'idle' | 'loading' | 'success' | 'error';

interface ServiceStatus {
    name: string;
    status: ConnectionStatus;
    message?: string;
}

export function BackendStatus() {
    const [services, setServices] = useState<ServiceStatus[]>([
        { name: 'Flight Service', status: 'idle' },
    ]);
    const [lastChecked, setLastChecked] = useState<string | null>(null);

    const checkFlightService = async () => {
        setServices(prev => prev.map(s =>
            s.name === 'Flight Service' ? { ...s, status: 'loading' as const } : s
        ));

        try {
            const response = await flightApi.hello();
            setServices(prev => prev.map(s =>
                s.name === 'Flight Service'
                    ? { ...s, status: 'success' as const, message: response }
                    : s
            ));
        } catch (error) {
            setServices(prev => prev.map(s =>
                s.name === 'Flight Service'
                    ? { ...s, status: 'error' as const, message: error instanceof Error ? error.message : 'Connection failed' }
                    : s
            ));
        }
    };

    const checkAllServices = async () => {
        await checkFlightService();
        setLastChecked(new Date().toLocaleTimeString());
    };

    useEffect(() => {
        // Initial check on mount
        checkAllServices();
    }, []);

    const getStatusIcon = (status: ConnectionStatus) => {
        switch (status) {
            case 'loading':
                return <Loader2 className="w-4 h-4 animate-spin text-blue-400" />;
            case 'success':
                return <CheckCircle className="w-4 h-4 text-green-400" />;
            case 'error':
                return <XCircle className="w-4 h-4 text-red-400" />;
            default:
                return <div className="w-4 h-4 rounded-full bg-slate-600" />;
        }
    };

    const getStatusBadge = (status: ConnectionStatus) => {
        switch (status) {
            case 'loading':
                return <Badge variant="outline" className="bg-blue-950 text-blue-300 border-blue-800">Checking...</Badge>;
            case 'success':
                return <Badge variant="outline" className="bg-green-950 text-green-300 border-green-800">Connected</Badge>;
            case 'error':
                return <Badge variant="outline" className="bg-red-950 text-red-300 border-red-800">Offline</Badge>;
            default:
                return <Badge variant="outline" className="bg-slate-800 text-slate-300 border-slate-700">Unknown</Badge>;
        }
    };

    return (
        <Card className="bg-slate-900 border-slate-800">
            <div className="p-4 border-b border-slate-800 flex items-center justify-between">
                <div>
                    <h2 className="text-slate-100">Backend Services</h2>
                    {lastChecked && (
                        <p className="text-slate-500 text-xs mt-1">Last checked: {lastChecked}</p>
                    )}
                </div>
                <Button
                    onClick={checkAllServices}
                    size="sm"
                    variant="outline"
                    className="border-slate-700 text-slate-300 hover:bg-slate-800"
                >
                    <RefreshCw className="w-4 h-4 mr-2" />
                    Refresh
                </Button>
            </div>

            <div className="p-4 space-y-3">
                {services.map((service) => (
                    <div
                        key={service.name}
                        className="p-3 bg-slate-950 border border-slate-800 rounded-md"
                    >
                        <div className="flex items-center justify-between mb-2">
                            <div className="flex items-center gap-2">
                                {getStatusIcon(service.status)}
                                <span className="text-slate-200">{service.name}</span>
                            </div>
                            {getStatusBadge(service.status)}
                        </div>

                        {service.message && (
                            <p className={`text-xs mt-2 ${service.status === 'success' ? 'text-green-400' : 'text-red-400'
                                }`}>
                                {service.message}
                            </p>
                        )}
                    </div>
                ))}
            </div>
        </Card>
    );
}
