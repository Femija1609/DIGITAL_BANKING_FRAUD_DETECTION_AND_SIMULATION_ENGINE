export default function TransactionTable({ data }) {
  return (
    <table border="1" width="100%" style={{ marginTop: "20px" }}>
      <thead>
        <tr>
          <th>ID</th>
          <th>Sender</th>
          <th>Amount</th>
          <th>Status</th>
          <th>Fraud</th>
          <th>Risk</th>
        </tr>
      </thead>
      <tbody>
        {data.map(t => (
          <tr key={t.id}>
            <td>{t.transactionId}</td>
            <td>{t.senderAccount}</td>
            <td>{t.amount}</td>
            <td>{t.status}</td>
            <td>{t.fraudStatus}</td>
            <td>{t.riskScore}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
