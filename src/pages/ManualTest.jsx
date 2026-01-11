import { useState } from "react";

export default function ManualTest() {
  const [form, setForm] = useState({
    senderAccount: "",
    receiverAccount: "",
    amount: "",
    currency: "INR",
    country: "India",
    channel: "MOBILE",
    transactionType: "TRANSFER",
    deviceId: "",
    ipAddress: ""
  });

  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const simulateTransaction = async () => {
    setLoading(true);
    setResult(null);

    try {
      const res = await fetch(
        "http://localhost:8080/api/transactions/simulate",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            senderAccount: form.senderAccount,
            receiverAccount: form.receiverAccount,
            amount: Number(form.amount),
            currency: form.currency,
            country: form.country,
            channel: form.channel,
            transactionType: form.transactionType,
            deviceId: form.deviceId,
            ipAddress: form.ipAddress
          })
        }
      );

      const data = await res.json();
      setResult(data);
    } catch (err) {
      alert("Backend error. Check Spring Boot.");
      console.error(err);
    }

    setLoading(false);
  };

  return (
    <div className="dashboard">
      <h2>🧪 Manual Transaction Simulator</h2>
      <p>Simulate a transaction and observe fraud detection in real time</p>

      <div className="sim-form">
        <input
          name="senderAccount"
          placeholder="Sender Account"
          value={form.senderAccount}
          onChange={handleChange}
        />

        <input
          name="receiverAccount"
          placeholder="Receiver Account"
          value={form.receiverAccount}
          onChange={handleChange}
        />

        <input
          name="amount"
          type="number"
          placeholder="Amount"
          value={form.amount}
          onChange={handleChange}
        />

        <select name="currency" value={form.currency} onChange={handleChange}>
          <option value="INR">INR</option>
          <option value="USD">USD</option>
        </select>

        <select name="country" value={form.country} onChange={handleChange}>
          <option value="India">India</option>
          <option value="USA">USA</option>
          <option value="Unknown">Unknown</option>
        </select>

        <select name="channel" value={form.channel} onChange={handleChange}>
          <option value="MOBILE">MOBILE</option>
          <option value="WEB">WEB</option>
        </select>

        <select
          name="transactionType"
          value={form.transactionType}
          onChange={handleChange}
        >
          <option value="TRANSFER">TRANSFER</option>
          <option value="UPI">UPI</option>
          <option value="CARD">CARD</option>
        </select>

        <input
          name="deviceId"
          placeholder="Device ID (e.g., DEV-1001)"
          value={form.deviceId}
          onChange={handleChange}
        />

        <input
          name="ipAddress"
          placeholder="IP Address (e.g., 192.168.1.10)"
          value={form.ipAddress}
          onChange={handleChange}
        />

        <button onClick={simulateTransaction} disabled={loading}>
          {loading ? "Simulating..." : "Simulate Transaction"}
        </button>
      </div>

      {result && (
        <div className="result-box">
          <h3>🧾 Simulation Result</h3>

          <h4>Transaction Details</h4>
          <p><b>Transaction ID:</b> {result.transactionId}</p>
          <p><b>Timestamp:</b> {result.timestamp}</p>
          <p>
            <b>Sender → Receiver:</b>{" "}
            {result.senderAccount} → {result.receiverAccount}
          </p>
          <p>
            <b>Amount:</b> {result.amount} {result.currency}
          </p>
          <p><b>Channel:</b> {result.channel}</p>
          <p><b>Country:</b> {result.location}</p>
          <p><b>Transaction Type:</b> {result.transactionType}</p>
          <p><b>Device ID:</b> {result.deviceId}</p>
          <p><b>IP Address:</b> {result.ipAddress}</p>

          <hr />

          <h4>Fraud Analysis</h4>
          <p><b>Status:</b> {result.status}</p>

          {result.statusReason && (
            <p><b>Status Reason:</b> {result.statusReason}</p>
          )}

          <p><b>Fraud Status:</b> {result.fraudStatus}</p>
          <p><b>Risk Score:</b> {result.riskScore}</p>
          <p>
            <b>ML Prediction:</b>{" "}
            {result.mlPrediction === 1 ? "Fraud" : "Not Fraud"}
          </p>
          <p>
            <b>ML Probability:</b>{" "}
            {(result.mlProbability * 100).toFixed(1)}%
          </p>
        </div>
      )}
    </div>
  );
}
