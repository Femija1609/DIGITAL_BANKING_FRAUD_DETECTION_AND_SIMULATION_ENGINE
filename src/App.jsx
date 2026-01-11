import { Routes, Route, Navigate } from "react-router-dom";

import Dashboard from "./pages/Dashboard";
import Transactions from "./pages/Transactions";
import ManualTest from "./pages/ManualTest";
import FraudAlerts from "./pages/FraudAlerts";
import HighRisk from "./pages/HighRisk";
import Login from "./pages/Login";

import Layout from "./components/layout/PageWrapper";

// TEMP AUTH CHECK (OK FOR DEMO / VIVA)
const isAuthenticated = () => {
  return true;
};

export default function App() {
  return (
    <Routes>

      {/* LOGIN */}
      <Route path="/login" element={<Login />} />

      {/* DASHBOARD */}
      <Route
        path="/"
        element={
          isAuthenticated()
            ? <Layout><Dashboard /></Layout>
            : <Navigate to="/login" />
        }
      />

      {/* TRANSACTIONS */}
      <Route
        path="/transactions"
        element={
          isAuthenticated()
            ? <Layout><Transactions /></Layout>
            : <Navigate to="/login" />
        }
      />

      {/* FRAUD ALERTS */}
      <Route
        path="/fraud-alerts"
        element={
          isAuthenticated()
            ? <Layout><FraudAlerts /></Layout>
            : <Navigate to="/login" />
        }
      />

      {/* HIGH RISK */}
      <Route
        path="/high-risk"
        element={
          isAuthenticated()
            ? <Layout><HighRisk /></Layout>
            : <Navigate to="/login" />
        }
      />

      {/* MANUAL TEST */}
      <Route
        path="/manual-test"
        element={
          isAuthenticated()
            ? <Layout><ManualTest /></Layout>
            : <Navigate to="/login" />
        }
      />

      {/* FALLBACK */}
      <Route path="*" element={<Navigate to="/" />} />

    </Routes>
  );
}
