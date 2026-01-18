import { useState } from "react";
import api from "../services/api";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async () => {
    try {
      const res = await api.post("/auth/login", {
        username,
        password,
      });

      if (res.data) {
        localStorage.setItem("token", "demo-token"); // demo token
        window.location.href = "/";
      } else {
        alert("Invalid credentials");
      }
    } catch (err) {
      alert("Login failed");
    }
  };

  return (
    <div className="login-page">
      <div className="login-box">
          <h2 style={{ textAlign: "center", marginBottom: "20px" }}>

                    PAY-WATCH 🔰
                  </h2>
        <h2 style={{ textAlign: "center", marginBottom: "20px" }}>
          LOGIN
        </h2>

        <input
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          style={{ width: "100%", marginBottom: "12px" }}
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          style={{ width: "100%", marginBottom: "20px" }}
        />

        <button
          style={{ width: "100%" }}
          onClick={handleLogin}
        >
          LOGIN
        </button>
      </div>
    </div>
  );
}
