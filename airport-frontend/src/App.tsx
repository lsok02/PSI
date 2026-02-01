import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { SecurityDashboardPage, FlightsPage, FlightDetailPage } from '@/pages';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Security Dashboard */}
        <Route path="/security" element={<SecurityDashboardPage />} />

        {/* Flight Management */}
        <Route path="/flights" element={<FlightsPage />} />
        <Route path="/flights/:id" element={<FlightDetailPage />} />

        {/* Default redirect to flights */}
        <Route path="/" element={<Navigate to="/flights" replace />} />
        <Route path="*" element={<Navigate to="/flights" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
