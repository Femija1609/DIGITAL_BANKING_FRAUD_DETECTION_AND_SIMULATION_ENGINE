import { useEffect, useState } from "react";
import api from "../services/api";
import TransactionTable from "../components/transactions/TransactionTable";

export default function HighRisk() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api
      .get("/transactions")
      .then(res => {
        // ONLY HIGH RISK TRANSACTIONS
        const highRisk = res.data.filter(
          t => t.riskScore !== null && t.riskScore >= 70
        );
        setData(highRisk);
      })
      .catch(err => console.error("High Risk error:", err))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="dashboard">
      <h2>⚠️ High Risk Transactions</h2>

      <p style={{ color: "var(--text-muted)", marginBottom: "16px" }}>
        Transactions with <b>risk score ≥ 70</b>
      </p>

      {loading ? (
        <p>Loading high-risk transactions...</p>
      ) : data.length === 0 ? (
        <p>No high-risk transactions found.</p>
      ) : (
        <TransactionTable data={data} />
      )}
    </div>
  );
}
