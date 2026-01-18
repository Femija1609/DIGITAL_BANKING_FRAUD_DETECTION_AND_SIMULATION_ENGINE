import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  CartesianGrid
} from "recharts";

export default function FraudBar({ data }) {
  const chartData = [
    { name: "SUCCESS", value: data.success || 0 },
    { name: "PENDING", value: data.pending || 0 },
    { name: "FAILED", value: data.failed || 0 },
    { name: "FRAUD", value: data.fraud || 0 },
  ];

  return (
    <div className="chart-card">
      <h3> TRANSACTION TREND</h3>

      {/* ✅ FIX: wrapper that actually moves the chart */}
      <div className="line-chart-shift">
        <ResponsiveContainer width="100%" height={260}>
          <LineChart data={chartData}>
            <CartesianGrid strokeDasharray="3 3" stroke="var(--border)" />
            <XAxis dataKey="name" stroke="var(--text-muted)" />
            <YAxis stroke="var(--text-muted)" />
            <Tooltip />

            <Line
              type="monotone"
              dataKey="value"
              stroke="var(--cyan)"
              strokeWidth={3}
              dot={{ r: 5 }}
              activeDot={{ r: 7 }}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}
