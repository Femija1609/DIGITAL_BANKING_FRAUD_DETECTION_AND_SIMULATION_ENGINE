import { useEffect, useState } from "react";
import api from "../services/api";
import SummaryCards from "../components/SummaryCards";
import FilterPanel from "../components/FilterPanel";

export default function Dashboard() {

  const [summary, setSummary] = useState(null);
  const [transactions, setTransactions] = useState([]);

  // ----------------------------
  // LOAD SUMMARY
  // ----------------------------
  const loadSummary = async () => {
    try {
      const res = await api.get("/summary");
      setSummary(res.data);
    } catch (err) {
      console.error("Failed to load summary", err);
    }
  };

  // ----------------------------
  // LOAD TRANSACTIONS (FILTER / ALL)
  // ----------------------------
  const loadTransactions = async (filters = {}) => {
    try {
      const res = await api.get("/filter", { params: filters });
      setTransactions(res.data);
    } catch (err) {
      console.error("Failed to load transactions", err);
    }
  };

  // ----------------------------
  // INITIAL LOAD
  // ----------------------------
  useEffect(() => {
    loadSummary();          // default summary
    loadTransactions({});  // load ALL transactions
  }, []);

  return (
    <div style={{ width: "100vw", minHeight: "100vh", background: "#f5f7fb" }}>

      {/* LOGOUT BUTTON */}
      <button
        onClick={() => {
          localStorage.removeItem("user");
          window.location.reload();
        }}
        style={{
          position: "absolute",
          top: "20px",
          right: "20px",
          background: "red",
          color: "white",
          border: "none",
          padding: "10px 14px",
          borderRadius: "6px",
          cursor: "pointer"
        }}
      >
        Logout
      </button>

      <div style={{ maxWidth: "1400px", margin: "auto", padding: "20px" }}>

        <h2 style={{ color: "#0d47a1", marginBottom: "10px" }}>
          💳 Digital Banking Fraud Detection Dashboard
        </h2>

        {/* SUMMARY CARDS */}
        {summary && <SummaryCards summary={summary} />}

        {/* FILTER PANEL */}
        <FilterPanel onSearch={loadTransactions} />

        {/* TRANSACTION TABLE */}
        <div style={{ overflowX: "auto" }}>
          <table
            border="1"
            width="100%"
            style={{
              borderCollapse: "collapse",
              background: "white"
            }}
          >
            <thead>
              <tr>
                <th style={th}>Txn ID</th>
                <th style={th}>Sender</th>
                <th style={th}>Receiver</th>
                <th style={th}>Amount</th>
                <th style={th}>Currency</th>
                <th style={th}>Status</th>
                <th style={th}>Fraud</th>
                <th style={th}>Risk</th>
                <th style={th}>ML Fraud</th>
                <th style={th}>ML Risk %</th>
              </tr>
            </thead>

            <tbody>
              {transactions.map((tx) => (
                <tr key={tx.id}>
                  <td style={td}>{tx.transactionId}</td>
                  <td style={td}>{tx.senderAccount}</td>
                  <td style={td}>{tx.receiverAccount}</td>
                  <td style={td}>{tx.amount}</td>
                  <td style={td}>{tx.currency}</td>
                  <td style={td}>{tx.status}</td>
                  <td style={td}>{tx.fraudStatus}</td>
                  <td style={td}>{tx.riskScore}</td>

                  {/* ML RESULT */}
                  <td style={{ ...td, fontWeight: "bold" }}>
                    {tx.mlPrediction === 1 ? "YES" : "NO"}
                  </td>

                  <td style={td}>
                    {tx.mlProbability !== null && tx.mlProbability !== undefined
                      ? (tx.mlProbability * 100).toFixed(2) + "%"
                      : "-"}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

      </div>
    </div>
  );
}

// ----------------------------
// STYLES
// ----------------------------
const th = {
  padding: "10px",
  background: "#eeeeee",
  color: "black",
  textAlign: "center"
};

const td = {
  padding: "8px",
  color: "black",
  textAlign: "center"
};
