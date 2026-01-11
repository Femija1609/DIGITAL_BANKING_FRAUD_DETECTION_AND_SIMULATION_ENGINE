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
    api.get("/transactions").then(res => setData(res.data));
  };

  const applyFilters = () => {
    console.log("🟢 APPLY FILTERS:", filters);

    // 🔑 REMOVE EMPTY FILTERS (VERY IMPORTANT)
    const params = {};
    Object.entries(filters).forEach(([key, value]) => {
      if (value && value.trim() !== "") {
        params[key] = value;
      }
    });

    console.log("🟡 FINAL QUERY PARAMS:", params);

    api
      .get("/transactions/filter", { params })
      .then(res => {
        console.log("🔵 FILTER RESULT:", res.data.length);
        setData(res.data);
      })
      .catch(err => {
        console.error("❌ FILTER ERROR:", err);
        alert("Backend filter failed. Check console.");
      });
  };

  useEffect(() => {
    loadAll();
  }, []);

  return (
    <div className="dashboard">
      <h2>All Transactions</h2>

      <TransactionFilters
        filters={filters}
        setFilters={setFilters}
        onApply={applyFilters}
      />

      <TransactionTable data={data} />
    </div>
  );
}
