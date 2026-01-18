import { useEffect, useState } from "react";
import api from "../services/api";
import TransactionTable from "../components/transactions/TransactionTable";
import TransactionFilters from "../components/transactions/TransactionFilters";

export default function Transactions() {
  const [data, setData] = useState([]);
  const [filters, setFilters] = useState({
    senderAccount: "",
    receiverAccount: "",
    fraudStatus: "",
    status: "",
  });

  const loadAll = () => {
    api.get("/transactions")
      .then(res => setData(res.data));
  };

  const applyFilters = () => {
    const params = {};
    Object.entries(filters).forEach(([key, value]) => {
      if (value && value.trim() !== "") {
        params[key] = value;
      }
    });

    api
      .get("/transactions/filter", { params })
      .then(res => setData(res.data))
      .catch(() => alert("Backend filter failed"));
  };

  useEffect(() => {
    loadAll();

    // ✅ FIX: clear ONLY transaction inbox (fraudStatus = OK)
    api.post("/notifications/mark-seen", null, {
      params: { type: "transactions" }
    }).catch(() => {});
  }, []);

  return (
    <div className="dashboard">
      <h2>ALL TRANSACTIONS</h2>
      <h4>FILTERS & CONTROLS ▼</h4>

      <TransactionFilters
        filters={filters}
        setFilters={setFilters}
        onApply={applyFilters}
      />

      <TransactionTable data={data} />
    </div>
  );
}
