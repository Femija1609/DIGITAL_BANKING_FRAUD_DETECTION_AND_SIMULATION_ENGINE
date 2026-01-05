export default function Home() {
  return (
    <div style={{ padding: "40px" }}>
      <h1>Welcome to Digital Banking Fraud Monitoring</h1>
      <p>
        This system continuously monitors transactions, detects fraud,
        alerts users, and provides real-time analytics via dashboards.
      </p>

      <h3>How it works:</h3>
      <ol>
        <li>Transaction enters API</li>
        <li>Fraud rules are evaluated</li>
        <li>Status & fraud flags assigned</li>
        <li>Email alert sent if required</li>
        <li>Dashboard updates instantly</li>
      </ol>
    </div>
  );
}
