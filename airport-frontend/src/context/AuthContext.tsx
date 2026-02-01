import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import { authApi, getToken, removeToken } from '@/api';

interface AuthContextType {
    isAuthenticated: boolean;
    username: string | null;
    login: (username: string, password: string) => Promise<{ success: boolean; message: string }>;
    logout: () => void;
    isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [username, setUsername] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    // Check token on mount
    useEffect(() => {
        const token = getToken();
        if (token) {
            // In production, you'd validate the token with the server
            // For now, just check if it exists
            setIsAuthenticated(true);
            // Decode username from token if needed (JWT payload)
            try {
                const payload = JSON.parse(atob(token.split('.')[1]));
                setUsername(payload.sub || null);
            } catch {
                setUsername(null);
            }
        }
        setIsLoading(false);
    }, []);

    const login = async (usernameInput: string, password: string) => {
        try {
            const response = await authApi.login({ username: usernameInput, password });
            if (response.token) {
                setIsAuthenticated(true);
                setUsername(usernameInput);
                return { success: true, message: response.message };
            }
            return { success: false, message: response.message || 'Login failed' };
        } catch (error) {
            console.error('Login error:', error);
            return { success: false, message: 'Network error. Please try again.' };
        }
    };

    const logout = () => {
        removeToken();
        setIsAuthenticated(false);
        setUsername(null);
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, username, login, logout, isLoading }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
}
