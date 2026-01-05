import { useState } from "react";
import Dashboard from "./pages/Dashboard";
import Login from "./pages/Login";

function App() {
  const [user, setUser] = useState(
    JSON.parse(localStorage.getItem("user"))
  );

  // 🔐 Show Login if NOT logged in
  if (!user) {
    return <Login setUser={setUser} />;
  }

  // ✅ Show Dashboard if logged in
  return <Dashboard setUser={setUser} />;
}

export default App;
