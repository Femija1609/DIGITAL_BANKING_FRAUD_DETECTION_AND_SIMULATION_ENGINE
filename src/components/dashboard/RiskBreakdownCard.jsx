import { useEffect, useState } from "react";
import api from "../../services/api";

export default function RiskBreakdownCard() {
  const [data, setData] = useState({
    ok: 0,
    suspicious: 0,
    fraud: 0,
    total: 0,
  });

  useEffect(() => {
    api
      .get("/transactions")
      .then((res) => {
        const txs = res.data || [];

        const ok = txs.filter((t) => t.fraudStatus === "OK").length;
        const suspicious = txs.filter(
          (t) => t.fraudStatus === "SUSPICIOUS"
        ).length;
        const fraud = txs.filter((t) => t.fraudStatus === "FRAUD").length;

        const total = ok + suspicious + fraud;

        setData({ ok, suspicious, fraud, total });
      })
      .catch(() => {});
  }, []);

  const percent = (value) =>
    data.total === 0 ? 0 : Math.round((value / data.total) * 100);

  return (
    <div className="risk-feed">
      <h3> RISK BREAKDOWN </h3>

      <div className="risk-item">
        <div>
          🟢 LOW RISK
          <span className="risk-amount">{percent(data.ok)}%</span>
        </div>
        <div className="risk-meta">
          <span className="muted">Fraud Status: OK</span>
        </div>
      </div>

      <div className="risk-item">
        <div>
          🟡 SUSPICIOUS
          <span className="risk-amount">
            {percent(data.suspicious)}%
          </span>
        </div>
        <div className="risk-meta">
          <span className="muted">Needs review</span>
        </div>
      </div>

      <div className="risk-item">
        <div>
          🔴 FRAUD
          <span className="risk-amount">{percent(data.fraud)}%</span>
        </div>
        <div className="risk-meta">
          <span className="muted">High risk detected</span>
        </div>
      </div>
    </div>
  );
}
