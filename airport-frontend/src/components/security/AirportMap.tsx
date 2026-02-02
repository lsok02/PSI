import { MapPin } from 'lucide-react';
import { Card } from '@/components/ui/card';
import type { Incident } from '@/types';

interface AirportMapProps {
    incidents: Incident[];
    selectedIncidentId: string;
    onIncidentSelect: (incidentId: string) => void;
}

export function AirportMap({ incidents, selectedIncidentId, onIncidentSelect }: AirportMapProps) {
    const getIncidentColor = (priority: string) => {
        switch (priority) {
            case 'Critical':
                return 'bg-red-500 border-red-400 shadow-red-500/50';
            case 'High':
                return 'bg-orange-500 border-orange-400 shadow-orange-500/50';
            case 'Medium':
                return 'bg-yellow-500 border-yellow-400 shadow-yellow-500/50';
            default:
                return 'bg-blue-500 border-blue-400 shadow-blue-500/50';
        }
    };

    return (
        <Card className="bg-slate-900 border-slate-800 h-full flex flex-col overflow-hidden">
            <div className="p-4 border-b border-slate-800 shrink-0">
                <h2 className="text-slate-100">Airport Map - Live View</h2>
                <p className="text-slate-400 text-xs mt-1">Terminal A, B & C - Main Concourse</p>
            </div>

            <div className="flex-1 p-4 min-h-0 flex flex-col">
                <div className="relative flex-1 min-h-0 bg-slate-950 border border-slate-800 rounded-lg overflow-hidden">
                    {/* Airport Layout */}
                    <svg className="w-full h-full" viewBox="0 0 100 100" preserveAspectRatio="xMidYMid meet">
                        {/* Terminal Buildings */}
                        <rect x="10" y="15" width="25" height="35" fill="#1e293b" stroke="#334155" strokeWidth="0.5" />
                        <text x="22.5" y="33" fill="#64748b" fontSize="3" textAnchor="middle">Terminal A</text>

                        <rect x="10" y="55" width="25" height="35" fill="#1e293b" stroke="#334155" strokeWidth="0.5" />
                        <text x="22.5" y="73" fill="#64748b" fontSize="3" textAnchor="middle">Terminal B</text>

                        <rect x="40" y="35" width="25" height="30" fill="#1e293b" stroke="#334155" strokeWidth="0.5" />
                        <text x="52.5" y="51" fill="#64748b" fontSize="3" textAnchor="middle">Terminal C</text>

                        {/* Gates */}
                        <rect x="70" y="20" width="20" height="8" fill="#1e293b" stroke="#334155" strokeWidth="0.4" />
                        <text x="80" y="25" fill="#64748b" fontSize="2.5" textAnchor="middle">Gate G15</text>

                        <rect x="70" y="32" width="20" height="8" fill="#1e293b" stroke="#334155" strokeWidth="0.4" />
                        <text x="80" y="37" fill="#64748b" fontSize="2.5" textAnchor="middle">Gates G16-G20</text>

                        {/* Checkpoints */}
                        <rect x="38" y="70" width="14" height="8" fill="#1e293b" stroke="#334155" strokeWidth="0.4" />
                        <text x="45" y="75" fill="#64748b" fontSize="2" textAnchor="middle">Checkpoint C</text>

                        {/* Baggage Area */}
                        <rect x="15" y="82" width="15" height="10" fill="#1e293b" stroke="#334155" strokeWidth="0.4" />
                        <text x="22.5" y="88" fill="#64748b" fontSize="2" textAnchor="middle">Baggage</text>

                        {/* Maintenance */}
                        <rect x="75" y="65" width="15" height="10" fill="#1e293b" stroke="#334155" strokeWidth="0.4" />
                        <text x="82.5" y="71" fill="#64748b" fontSize="2" textAnchor="middle">Maint. Bay 7</text>

                        {/* Runways */}
                        <line x1="5" y1="10" x2="95" y2="10" stroke="#334155" strokeWidth="2" strokeDasharray="2,2" />
                        <line x1="5" y1="95" x2="95" y2="95" stroke="#334155" strokeWidth="2" strokeDasharray="2,2" />
                    </svg>

                    {/* Incident Markers */}
                    {incidents.map((incident) => (
                        <div
                            key={incident.id}
                            onClick={() => onIncidentSelect(incident.id)}
                            className={`absolute cursor-pointer transition-all ${selectedIncidentId === incident.id ? 'z-20 scale-125' : 'z-10'
                                }`}
                            style={{
                                left: `${incident.coordinates.x}%`,
                                top: `${incident.coordinates.y}%`,
                                transform: 'translate(-50%, -50%)'
                            }}
                        >
                            <div className={`relative ${getIncidentColor(incident.priority)} w-6 h-6 rounded-full border-2 shadow-lg animate-pulse`}>
                                <div className="absolute inset-0 flex items-center justify-center">
                                    <MapPin className="w-4 h-4 text-white" />
                                </div>
                                {selectedIncidentId === incident.id && (
                                    <div className="absolute -top-14 left-1/2 -translate-x-1/2 bg-slate-900 border border-slate-700 p-2 rounded shadow-2xl text-[10px] whitespace-nowrap z-30 pointer-events-none">
                                        <div className="font-bold text-blue-400">{incident.id}</div>
                                        <div className="text-slate-100">{incident.type}</div>
                                        <div className="text-slate-400">{incident.location}</div>
                                    </div>
                                )}
                            </div>
                            <div className={`absolute inset-0 ${getIncidentColor(incident.priority)} rounded-full animate-ping opacity-75`}></div>
                        </div>
                    ))}
                </div>

                {/* Legend */}
                <div className="mt-4 shrink-0 flex items-center gap-6 text-xs text-slate-400">
                    <div className="flex items-center gap-2">
                        <div className="w-3 h-3 rounded-full bg-red-500"></div>
                        <span>Critical</span>
                    </div>
                    <div className="flex items-center gap-2">
                        <div className="w-3 h-3 rounded-full bg-orange-500"></div>
                        <span>High</span>
                    </div>
                    <div className="flex items-center gap-2">
                        <div className="w-3 h-3 rounded-full bg-yellow-500"></div>
                        <span>Medium</span>
                    </div>
                </div>
            </div>
        </Card>
    );
}
