import { useEffect, useState } from "react";
import api from "../services/api";

import MetricCard from "../components/dashboard/MetricCard";
import StatusPie from "../components/dashboard/StatusPie";
import FraudBar from "../components/dashboard/FraudBar";

// ✅ NEW
import RiskBreakdownCard from "../components/dashboard/RiskBreakdownCard";

export default function Dashboard() {
  const [summary, setSummary] = useState({});

  // ===============================
  // LOAD DASHBOARD SUMMARY
  // ===============================
  const loadSummary = () => {
    api
      .get("/transactions/summary")
      .then((res) => setSummary(res.data))
      .catch((err) => console.error("Summary error:", err));
  };

  useEffect(() => {
    loadSummary();
  }, []);

  // ===============================
  // GENERATE TRANSACTION
  // ===============================
  const generateTransaction = async () => {
    try {
      const payload = {
        senderAccount: "ACC" + Math.floor(Math.random() * 100000),
        receiverAccount: "ACC" + Math.floor(Math.random() * 100000),
        amount: Math.floor(Math.random() * 100000) + 500,
        currency: Math.random() > 0.5 ? "INR" : "USD",
        country: Math.random() > 0.7 ? "USA" : "India",
        channel: Math.random() > 0.5 ? "WEB" : "MOBILE",
        transactionType: "TRANSFER",
        deviceId: "DEV-" + Math.floor(Math.random() * 9999),
        ipAddress: "192.168.1." + Math.floor(Math.random() * 255),
      };

      await api.post("/transactions/simulate", payload);

      // Reload summary after generation
      loadSummary();
    } catch (err) {
      console.error("Generate transaction failed:", err);
      alert("Backend error. Is Spring Boot running?");
    }
  };

  return (
    <div className="dashboard">

      {/* ================= METRIC CARDS ================= */}
      <div className="grid">
        <MetricCard title="TOTAL" value={summary.total || 0} color="var(--cyan)" />
        <MetricCard title="SUCCESS" value={summary.success || 0} color="var(--success)" />
        <MetricCard title="FAILED" value={summary.failed || 0} color="var(--danger)" />
        <MetricCard title="FRAUD" value={summary.fraud || 0} color="var(--warning)" />
      </div>

      {/* ================= CHARTS + SIDE CARD ================= */}
      <div
        className="chart-grid"
        style={{
          display: "grid",
          gridTemplateColumns: "2fr 2fr 1.2fr",
          gap: "16px",
          alignItems: "stretch",
        }}
      >
        <StatusPie data={summary} />
        <FraudBar data={summary} />
        <RiskBreakdownCard />
      </div>

      {/* ================= GENERATE TRANSACTION ================= */}
      <div className="generate-btn-wrapper">
        <button className="generate-btn" onClick={generateTransaction}>
          GENERATE TRANSACTION
        </button>
      </div>


    </div>
  );
}
