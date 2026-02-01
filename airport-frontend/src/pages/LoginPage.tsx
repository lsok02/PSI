import { useState } from 'react';
import { useNavigate, Navigate } from 'react-router-dom';
import { useAuth } from '@/context';
import { Card } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Plane, Lock, User, AlertCircle, Loader2 } from 'lucide-react';

export function LoginPage() {
    const navigate = useNavigate();
    const { login, isAuthenticated, isLoading } = useAuth();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    // If already authenticated, redirect to flights
    if (isLoading) {
        return (
            <div className="h-screen bg-slate-950 flex items-center justify-center">
                <Loader2 className="w-8 h-8 text-blue-500 animate-spin" />
            </div>
        );
    }

    if (isAuthenticated) {
        return <Navigate to="/flights" replace />;
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setIsSubmitting(true);

        const result = await login(username, password);

        if (result.success) {
            navigate('/flights');
        } else {
            setError(result.message);
        }

        setIsSubmitting(false);
    };

    return (
        <div className="h-screen bg-slate-950 flex items-center justify-center p-4">
            <div className="w-full max-w-md">
                {/* Header */}
                <div className="text-center mb-8">
                    <div className="inline-flex items-center justify-center w-16 h-16 rounded-full bg-blue-600 mb-4">
                        <Plane className="w-8 h-8 text-white" />
                    </div>
                    <h1 className="text-2xl font-semibold text-slate-100">Airport Management</h1>
                    <p className="text-slate-400 mt-2">Sign in to access the system</p>
                </div>

                {/* Login Form */}
                <Card className="bg-slate-900 border-slate-800 p-6">
                    <form onSubmit={handleSubmit} className="space-y-4">
                        {error && (
                            <div className="flex items-center gap-2 p-3 bg-red-950/50 border border-red-800 rounded-md text-red-400">
                                <AlertCircle className="w-4 h-4 flex-shrink-0" />
                                <span className="text-sm">{error}</span>
                            </div>
                        )}

                        <div className="space-y-2">
                            <Label htmlFor="username" className="text-slate-300">Username</Label>
                            <div className="relative">
                                <User className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
                                <Input
                                    id="username"
                                    type="text"
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)}
                                    placeholder="Enter your username"
                                    className="bg-slate-950 border-slate-700 text-slate-100 pl-10"
                                    required
                                />
                            </div>
                        </div>

                        <div className="space-y-2">
                            <Label htmlFor="password" className="text-slate-300">Password</Label>
                            <div className="relative">
                                <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
                                <Input
                                    id="password"
                                    type="password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    placeholder="Enter your password"
                                    className="bg-slate-950 border-slate-700 text-slate-100 pl-10"
                                    required
                                />
                            </div>
                        </div>

                        <Button
                            type="submit"
                            disabled={isSubmitting}
                            className="w-full bg-blue-600 hover:bg-blue-700 text-white"
                        >
                            {isSubmitting ? (
                                <>
                                    <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                                    Signing in...
                                </>
                            ) : (
                                'Sign In'
                            )}
                        </Button>
                    </form>

                    {/* Demo credentials hint */}
                    <div className="mt-6 pt-4 border-t border-slate-800">
                        <p className="text-xs text-slate-500 text-center mb-2">Demo credentials:</p>
                        <div className="flex justify-center gap-4 text-xs text-slate-400">
                            <span>admin / admin123</span>
                            <span>pilot / pilot123</span>
                        </div>
                    </div>
                </Card>
            </div>
        </div>
    );
}
