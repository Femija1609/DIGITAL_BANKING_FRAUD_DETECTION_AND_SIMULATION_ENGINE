import { Navigate } from "react-router-dom";
import { getUser } from "../../services/auth";

export default function ProtectedRoute({ children }) {
  return getUser() ? children : <Navigate to="/login" />;
}
