import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from '@/context';
import { SecurityDashboardPage, FlightsPage, FlightDetailPage, LoginPage } from '@/pages';
import { Loader2 } from 'lucide-react';

// Protected route wrapper
function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className="h-screen bg-slate-950 flex items-center justify-center">
        <Loader2 className="w-8 h-8 text-blue-500 animate-spin" />
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
}

function AppRoutes() {
  return (
    <Routes>
      {/* Public route */}
      <Route path="/login" element={<LoginPage />} />

      {/* Protected routes */}
      <Route
        path="/security"
        element={
          <ProtectedRoute>
            <SecurityDashboardPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/flights"
        element={
          <ProtectedRoute>
            <FlightsPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/flights/:id"
        element={
          <ProtectedRoute>
            <FlightDetailPage />
          </ProtectedRoute>
        }
      />

      {/* Default redirect */}
      <Route path="/" element={<Navigate to="/flights" replace />} />
      <Route path="*" element={<Navigate to="/flights" replace />} />
    </Routes>
  );
}

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
