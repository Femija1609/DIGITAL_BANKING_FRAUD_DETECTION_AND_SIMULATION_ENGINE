export default function MetricCard({ title, value, color }) {
  return (
    <div className="metric-card">
      <p className="metric-title">{title}</p>
      <h2 style={{ color }}>{value ?? "--"}</h2>
    </div>
  );
}
