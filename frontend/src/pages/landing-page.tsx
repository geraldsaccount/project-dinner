import { useAuthenticatedApi } from "@/hooks";
import { useEffect } from "react";

export interface ProtectedData {
  message: string;
  authenticatedUser: string;
  userId: string;
  email?: string;
  username?: string;
  fullname?: string;
}

const LandingPage = () => {
  const {
    callApi: fetchUserData,
    data: userData,
    loading,
    error,
  } = useAuthenticatedApi<ProtectedData>();
  useEffect(() => {
    fetchUserData("/api/protected/user");
  }, [fetchUserData]);

  return (
    <div>
      {loading && <p>Loading protected data...</p>}
      {error && <p style={{ color: "red" }}>Error: {error}</p>}
      {userData && (
        <div>
          <p>Message: {userData.message}</p>
          <p>Authenticated User: {userData.authenticatedUser}</p>
          <p>User ID: {userData.userId}</p>
          {userData.email && <p>Email: {userData.email}</p>}
          {userData.username && <p>Username: {userData.username}</p>}
          {userData.fullname && <p>Fullname: {userData.fullname}</p>}
        </div>
      )}
    </div>
  );
};

export default LandingPage;
