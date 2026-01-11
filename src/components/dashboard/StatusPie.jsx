import { PieChart, Pie, Cell, Tooltip } from "recharts";

const COLORS = ["#22c55e", "#ef4444", "#f59e0b"];

export default function StatusPie({ data }) {
  const chartData = [
    { name: "Success", value: data.success || 0 },
    { name: "Failed", value: data.failed || 0 },
    { name: "Pending", value: data.pending || 0 },
  ];

  return (
    <div className="chart-card">
      <h3>Transaction Status</h3>

      <PieChart width={260} height={260}>
        <Pie
          data={chartData}
          dataKey="value"
          cx="50%"
          cy="50%"
          outerRadius={90}
          label
        >
          {chartData.map((_, i) => (
            <Cell key={i} fill={COLORS[i]} />
          ))}
        </Pie>
        <Tooltip />
      </PieChart>
    </div>
  );
}
