import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

export default function ProtectedRoute({ allowedRoles }) {
  const { isAuthenticated, role } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(role)) {
    const fallback = role === "TRANSPORTER" ? "/transporter/dashboard" : role === "ADMIN" ? "/admin/cities" : "/customer/search";
    return <Navigate to={fallback} replace />;
  }

  return <Outlet />;
}
