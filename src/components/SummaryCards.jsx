export default function SummaryCards({ summary }) {
  return (
    <div
      style={{
        display: "grid",
        gridTemplateColumns: "repeat(auto-fit, minmax(150px, 1fr))",
        gap: "16px",
        marginBottom: "20px",
      }}
    >
      {Object.entries(summary).map(([key, value]) => (
        <div
          key={key}
          style={{
            background: "#1976d2",
            color: "white",
            padding: "16px",
            borderRadius: "8px",
            textAlign: "center",
            fontSize: "18px",
          }}
        >
          <strong>{key.toUpperCase()}</strong>
          <div style={{ fontSize: "22px", marginTop: "6px" }}>{value}</div>
        </div>
      ))}
    </div>
  );
}
