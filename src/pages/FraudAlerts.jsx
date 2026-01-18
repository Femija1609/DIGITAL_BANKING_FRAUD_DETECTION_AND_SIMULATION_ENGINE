import { useEffect, useState } from "react";
import api from "../services/api";
import TransactionTable from "../components/transactions/TransactionTable";

export default function FraudAlerts() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // 🔔 FIX: mark ONLY FRAUD notifications as seen
    api.post("/notifications/mark-seen", null, {
      params: { type: "fraud" }
    }).catch(() => {});

    api
      .get("/transactions/filter", {
        params: { fraudStatus: "FRAUD" }
      })
      .then(res => setData(res.data || []))
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="dashboard">
      <h2>FRAUD ALERTS</h2>

      <p style={{ color: "var(--text-muted)", marginBottom: "16px" }}>
        Transactions flagged as <b>FRAUD</b>
      </p>

      {loading ? (
        <p>Loading fraud transactions...</p>
      ) : data.length === 0 ? (
        <p>No fraud transactions found.</p>
      ) : (
        <TransactionTable data={data} />
      )}
    </div>
  );
}
