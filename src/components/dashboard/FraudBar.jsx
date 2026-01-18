import { BarChart, Bar, XAxis, YAxis, Tooltip } from "recharts";

export default function FraudBar({ data }) {
  const chartData = [
    { name: "Fraud", value: data.fraud || 0 },
    { name: "Suspicious", value: data.suspicious || 0 },
  ];

  return (
    <div className="chart-card">
      <h3>FRAUD DETECTION</h3>

      <BarChart width={300} height={220} data={chartData}>
        <XAxis dataKey="name" />
        <YAxis />
        <Tooltip />
        <Bar dataKey="value" fill="#ef4444" />
      </BarChart>
    </div>
  );
}
