export default function Badge({ type, value }) {
  const colors = {
    SUCCESS: "badge-success",
    FAILED: "badge-danger",
    PENDING: "badge-warning",
    FRAUD: "badge-danger",
    SUSPICIOUS: "badge-warning",
    OK: "badge-success",
  };

  return (
    <span className={`badge ${colors[value] || ""}`}>
      {value}
    </span>
  );
}
