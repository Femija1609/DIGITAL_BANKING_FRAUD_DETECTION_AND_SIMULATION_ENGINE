export default function Login({ setUser }) {

  const handleLogin = () => {
    const user = { username: "admin" };
    localStorage.setItem("user", JSON.stringify(user));
    setUser(user);
  };

  return (
    <div style={{
      width: "100vw",
      height: "100vh",
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      background: "#0d47a1"
    }}>
      <div style={{
        background: "white",
        padding: "40px",
        borderRadius: "10px",
        width: "100%",
        maxWidth: "380px",
        boxShadow: "0 6px 25px rgba(0,0,0,0.3)"
      }}>
        <h2 style={{
          textAlign: "center",
          color: "#0d47a1",
          marginBottom: "20px"
        }}>
          Digital Banking Login
        </h2>

        <input
          placeholder="Username"
          style={{
            width: "100%",
            padding: "12px",
            marginBottom: "14px",
            fontSize: "16px"
          }}
        />

        <input
          type="password"
          placeholder="Password"
          style={{
            width: "100%",
            padding: "12px",
            marginBottom: "20px",
            fontSize: "16px"
          }}
        />

        <button
          onClick={handleLogin}
          style={{
            width: "100%",
            padding: "12px",
            background: "#0d47a1",
            color: "white",
            border: "none",
            borderRadius: "6px",
            fontSize: "16px",
            cursor: "pointer"
          }}
        >
          Login
        </button>
      </div>
    </div>
  );
}
