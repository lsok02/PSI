import { useParams, useNavigate, Link } from 'react-router-dom';
import { Shield, Plane, Wrench, ArrowLeft, LogOut, User } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { FlightEditForm } from '@/components/flights';
import { useAuth } from '@/context';
import type { Flight } from '@/types';

import { useFlight, useUpdateFlightStatus } from '@/hooks';

export function FlightDetailPage() {
    const { id: flightId } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const { logout, username } = useAuth();

    const id = Number(flightId);
    const { data: flight, isLoading, isError } = useFlight(id);
    const updateMutation = useUpdateFlightStatus();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    if (isLoading) {
        return (
            <div className="h-screen bg-slate-950 flex items-center justify-center text-slate-100">
                <div className="flex flex-col items-center gap-4">
                    <Plane className="w-8 h-8 text-blue-500 animate-pulse" />
                    <p className="text-lg">Loading flight details...</p>
                </div>
            </div>
        );
    }

    if (isError || !flight) {
        return (
            <div className="h-screen bg-slate-950 text-slate-100 flex items-center justify-center">
                <div className="text-center">
                    <h1 className="text-2xl font-semibold mb-4">{isError ? 'Error loading flight' : 'Flight not found'}</h1>
                    <Button onClick={() => navigate('/flights')} variant="outline">
                        Back to Flights
                    </Button>
                </div>
            </div>
        );
    }

    const handleSave = async (updatedFlight: Flight) => {
        try {
            await updateMutation.mutateAsync({
                id: updatedFlight.id,
                status: updatedFlight.status
            });
            console.log('Saved flight status:', updatedFlight.status);
            navigate('/flights');
        } catch (error) {
            console.error('Failed to update flight:', error);
        }
    };

    const handleCancel = () => {
        navigate('/flights');
    };

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
                <div className="max-w-4xl mx-auto">
                    {/* Back Button */}
                    <Button
                        onClick={() => navigate('/flights')}
                        variant="ghost"
                        className="mb-4 text-slate-400 hover:text-slate-100"
                    >
                        <ArrowLeft className="w-4 h-4 mr-2" />
                        Back to Flight List
                    </Button>

                    <FlightEditForm
                        flight={flight}
                        onSave={handleSave}
                        onCancel={handleCancel}
                    />
                </div>
            </main>
        </div>
    );
}
