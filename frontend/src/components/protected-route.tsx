import React from "react";
import { useAuth } from "@clerk/clerk-react";
import { Outlet } from "react-router-dom";
import RestrictedContentPage from "@/pages/restricted-content-page";

const ProtectedRoute: React.FC = () => {
  const { isLoaded, isSignedIn } = useAuth();

  if (!isLoaded) {
    return <div>Loading...</div>;
  } else if (!isSignedIn) {
    return <RestrictedContentPage />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
