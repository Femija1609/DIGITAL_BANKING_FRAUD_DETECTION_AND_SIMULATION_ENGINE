import Badge from "../common/Badge";

export default function TransactionTable({ data }) {
  return (
    <div style={{ marginTop: "24px" }}>
      <table className="txn-table">
        <thead>
          <tr>
            <th>Txn ID</th>
            <th>Sender</th>
            <th>Receiver</th>
            <th>Amount</th>
            <th>Status</th>
            <th>Fraud</th>
            <th>Risk</th>
            <th>ML %</th>
          </tr>
        </thead>

        <tbody>
          {data.map(tx => (
            <tr key={tx.id}>
              <td>{tx.transactionId}</td>
              <td>{tx.senderAccount}</td>
              <td>{tx.receiverAccount}</td>
              <td>₹{tx.amount}</td>

              <td>
                <Badge value={tx.status} />
              </td>

              <td>
                <Badge value={tx.fraudStatus} />
              </td>

              <td>{tx.riskScore}</td>

              <td
                style={{
                  color:
                    tx.mlProbability >= 0.8
                      ? "var(--danger)"
                      : "var(--success)",
                  fontWeight: "600",
                }}
              >
                {Math.round((tx.mlProbability || 0) * 100)}%
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
