import ProtectedRoute from "../components/ProtectedRoute";
import AdminLayout from "../layouts/AdminLayout";
import CustomerLayout from "../layouts/CustomerLayout";
import PublicLayout from "../layouts/PublicLayout";
import TransporterLayout from "../layouts/TransporterLayout";
import AdminCitiesPage from "../pages/AdminCitiesPage";
import AdminCityFormPage from "../pages/AdminCityFormPage";
import BookingDetailPage from "../pages/BookingDetailPage";
import CustomerBookingsPage from "../pages/CustomerBookingsPage";
import CustomerProfilePage from "../pages/CustomerProfilePage";
import CustomerSearchPage from "../pages/CustomerSearchPage";
import LandingPage from "../pages/LandingPage";
import LoginPage from "../pages/LoginPage";
import RegisterPage from "../pages/RegisterPage";
import SearchResultsPage from "../pages/SearchResultsPage";
import TransporterBookingDetailPage from "../pages/TransporterBookingDetailPage";
import TransporterBookingsPage from "../pages/TransporterBookingsPage";
import TransporterDashboardPage from "../pages/TransporterDashboardPage";
import TransporterLoginPage from "../pages/TransporterLoginPage";
import TransporterProfilePage from "../pages/TransporterProfilePage";
import TransporterRegisterPage from "../pages/TransporterRegisterPage";
import TransporterTrucksPage from "../pages/TransporterTrucksPage";
import TruckFormPage from "../pages/TruckFormPage";

export const appRoutes = [
  {
    element: <PublicLayout />,
    children: [
      { path: "/", element: <LandingPage /> },
      { path: "/login", element: <LoginPage /> },
      { path: "/register", element: <RegisterPage /> },
      { path: "/transporter/login", element: <TransporterLoginPage /> },
      { path: "/transporter/register", element: <TransporterRegisterPage /> },
    ],
  },
  {
    element: <ProtectedRoute allowedRoles={["CUSTOMER"]} />,
    children: [
      {
        element: <CustomerLayout />,
        children: [
          { path: "/customer/search", element: <CustomerSearchPage /> },
          { path: "/customer/results", element: <SearchResultsPage /> },
          { path: "/customer/bookings", element: <CustomerBookingsPage /> },
          { path: "/customer/bookings/:id", element: <BookingDetailPage /> },
          { path: "/customer/profile", element: <CustomerProfilePage /> },
        ],
      },
    ],
  },
  {
    element: <ProtectedRoute allowedRoles={["TRANSPORTER"]} />,
    children: [
      {
        element: <TransporterLayout />,
        children: [
          { path: "/transporter/dashboard", element: <TransporterDashboardPage /> },
          { path: "/transporter/trucks", element: <TransporterTrucksPage /> },
          { path: "/transporter/trucks/new", element: <TruckFormPage /> },
          { path: "/transporter/trucks/:id/edit", element: <TruckFormPage /> },
          { path: "/transporter/bookings", element: <TransporterBookingsPage /> },
          { path: "/transporter/bookings/:id", element: <TransporterBookingDetailPage /> },
          { path: "/transporter/profile", element: <TransporterProfilePage /> },
        ],
      },
    ],
  },
  {
    element: <ProtectedRoute allowedRoles={["ADMIN"]} />,
    children: [
      {
        element: <AdminLayout />,
        children: [
          { path: "/admin/cities", element: <AdminCitiesPage /> },
          { path: "/admin/cities/new", element: <AdminCityFormPage /> },
        ],
      },
    ],
  },
];
