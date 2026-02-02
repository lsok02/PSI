import { Link, useNavigate } from 'react-router-dom';
import { Shield, Plane, Wrench, LogOut, User } from 'lucide-react';
import { FlightsTable } from '@/components/flights';
import { Button } from '@/components/ui/button';
import { useAuth } from '@/context';

import { useFlights } from '@/hooks';

export function FlightsPage() {
    const { data: flights = [], isLoading, isError } = useFlights();
    const { logout, username } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    if (isLoading) {
        return (
            <div className="h-screen bg-slate-950 flex items-center justify-center text-slate-100">
                <div className="flex flex-col items-center gap-4">
                    <Plane className="w-8 h-8 text-blue-500 animate-pulse" />
                    <p className="text-lg">Loading flight schedule...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="h-screen bg-slate-950 text-slate-100 flex flex-col overflow-hidden">
            {/* Navigation Header */}
            <header className="shrink-0 border-b border-slate-800 bg-slate-900">
                <div className="px-6 py-4 flex items-center justify-between">
                    <h1 className="text-xl font-semibold text-slate-100">Airport Management System</h1>
                    <nav className="flex items-center gap-4">
                        <Link
                            to="/security"
                            className="flex items-center gap-2 px-4 py-2 rounded-md text-slate-400 hover:text-slate-100 hover:bg-slate-800 transition-colors"
                        >
                            <Shield className="w-4 h-4" />
                            Security
                        </Link>
                        <Link
                            to="/flights"
                            className="flex items-center gap-2 px-4 py-2 rounded-md bg-blue-600 text-white"
                        >
                            <Plane className="w-4 h-4" />
                            Flights
                        </Link>
                        <Link
                            to="/ground-ops"
                            className="flex items-center gap-2 px-4 py-2 rounded-md text-slate-400 hover:text-slate-100 hover:bg-slate-800 transition-colors"
                        >
                            <Wrench className="w-4 h-4" />
                            Zasoby
                        </Link>
                        <div className="h-6 w-px bg-slate-700" />
                        <div className="flex items-center gap-2 text-slate-400 text-sm">
                            <User className="w-4 h-4" />
                            <span>{username || 'User'}</span>
                        </div>
                        <Button
                            onClick={handleLogout}
                            variant="ghost"
                            size="sm"
                            className="text-slate-400 hover:text-red-400 hover:bg-slate-800"
                        >
                            <LogOut className="w-4 h-4" />
                        </Button>
                    </nav>
                </div>
            </header>

            {/* Main Content */}
            <main className="flex-1 min-h-0 p-6 overflow-auto">
                {isError ? (
                    <div className="bg-red-950/30 border border-red-900 text-red-400 p-4 rounded-md">
                        Failed to load flights. Please make sure the Flight Service is running.
                    </div>
                ) : (
                    <FlightsTable flights={flights} />
                )}
            </main>
        </div>
    );
}
