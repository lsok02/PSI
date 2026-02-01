import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { SecurityDashboardPage } from '@/pages';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Security Dashboard as the main page */}
        <Route path="/security" element={<SecurityDashboardPage />} />

        {/* Add more routes here as your app grows */}
        {/* Example future routes:
        <Route path="/flights" element={<FlightsPage />} />
        <Route path="/passengers" element={<PassengersPage />} />
        <Route path="/operations" element={<OperationsPage />} />
        */}

        {/* Default redirect to security dashboard */}
        <Route path="/" element={<Navigate to="/security" replace />} />
        <Route path="*" element={<Navigate to="/security" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
