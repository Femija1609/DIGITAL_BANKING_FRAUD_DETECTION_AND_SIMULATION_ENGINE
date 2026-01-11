import { useEffect, useState } from "react";
import api from "../services/api";
import TransactionTable from "../components/transactions/TransactionTable";

export default function FraudAlerts() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api
      .get("/transactions/filter", {
        params: { fraudStatus: "FRAUD" }
      })
      .then(res => {
        setData(res.data || []);
      })
      .catch(err => {
        console.error("Fraud Alerts fetch error:", err);
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="dashboard">
      <h2>🚨 Fraud Alerts</h2>

      <p style={{ color: "var(--text-muted)", marginBottom: "16px" }}>
        Transactions flagged as <b>FRAUD</b> by rule engine or ML model
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
