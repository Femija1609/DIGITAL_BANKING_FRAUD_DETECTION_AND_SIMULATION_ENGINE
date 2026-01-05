import { useState } from "react";

export default function FilterPanel({ onSearch }) {

  const [senderAccount, setSenderAccount] = useState("");
  const [receiverAccount, setReceiverAccount] = useState("");
  const [fraudStatus, setFraudStatus] = useState("");
  const [status, setStatus] = useState("");

  const handleSearch = () => {
    onSearch({
      senderAccount: senderAccount || null,
      receiverAccount: receiverAccount || null,
      fraudStatus: fraudStatus || null,
      status: status || null
    });
  };

  return (
    <div style={{
      display: "grid",
      gridTemplateColumns: "repeat(auto-fit, minmax(180px, 1fr))",
      gap: "12px",
      marginBottom: "20px"
    }}>

      <input
        placeholder="Sender Account"
        value={senderAccount}
        onChange={(e) => setSenderAccount(e.target.value)}
      />

      <input
        placeholder="Receiver Account"
        value={receiverAccount}
        onChange={(e) => setReceiverAccount(e.target.value)}
      />

      <select
        value={fraudStatus}
        onChange={(e) => setFraudStatus(e.target.value)}
      >
        <option value="">Fraud Status</option>
        <option value="FRAUD">FRAUD</option>
        <option value="SUSPICIOUS">SUSPICIOUS</option>
        <option value="OK">OK</option>
      </select>

      <select
        value={status}
        onChange={(e) => setStatus(e.target.value)}
      >
        <option value="">Transaction Status</option>
        <option value="SUCCESS">SUCCESS</option>
        <option value="FAILED">FAILED</option>
        <option value="PENDING">PENDING</option>
      </select>

      <button
        onClick={handleSearch}
        style={{
          background: "#1976D2",
          color: "white",
          border: "none",
          borderRadius: "6px",
          padding: "10px",
          cursor: "pointer"
        }}
      >
        🔍 Search
      </button>
    </div>
  );
}
