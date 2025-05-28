import { useState, useCallback } from "react";
import { useAuth } from "@clerk/clerk-react";

interface UseAuthenticatedApiResult<TResponse, TBody> {
  data: TResponse | null;
  loading: boolean;
  error: string | null;
  callApi: (
    endpoint: string,
    method?: "GET" | "POST" | "PUT" | "DELETE",
    body?: TBody
  ) => Promise<TResponse | undefined>;
}

/**
 * A generic hook for making authenticated API calls using Clerk JWTs.
 *
 * @template TResponse The expected type of the response data.
 * @template TBody The expected type of the request body (for POST/PUT).
 * @param {string} jwtTemplateName The name of the JWT template configured in Clerk (e.g., 'spring-boot').
 * @returns {UseAuthenticatedApiResult<TResponse, TBody>} An object containing data, loading state, error, and a callApi function.
 */
function useAuthenticatedApi<TResponse, TBody = Record<string, unknown>>(
  jwtTemplateName: string = "spring-boot-v1"
): UseAuthenticatedApiResult<TResponse, TBody> {
  const { getToken, isLoaded, isSignedIn } = useAuth();
  const [data, setData] = useState<TResponse | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const callApi = useCallback(
    async (
      endpoint: string,
      method: "GET" | "POST" | "PUT" | "DELETE" = "GET",
      body?: TBody
    ): Promise<TResponse | undefined> => {
      setLoading(true);
      setError(null);
      setData(null);

      if (!isLoaded || !isSignedIn) {
        setError("User not signed in or Clerk not loaded.");
        setLoading(false);
        return undefined;
      }

      try {
        const token = await getToken({ template: jwtTemplateName });

        if (!token) {
          setError("Failed to retrieve authentication token.");
          setLoading(false);
          return undefined;
        }

        const headers: HeadersInit = {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        };

        const config: RequestInit = {
          method,
          headers,
        };

        if (body && (method === "POST" || method === "PUT")) {
          config.body = JSON.stringify(body);
        }

        const response = await fetch(endpoint, config);

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(
            `HTTP error! Status: ${response.status} - ${errorText}`
          );
        }

        const result: TResponse = await response.json();
        setData(result);
        return result;
      } catch (err) {
        console.error("API call error:", err);
        if (err instanceof Error) {
          setError(err.message);
        } else {
          setError("An unknown error occurred.");
        }
        return undefined;
      } finally {
        setLoading(false);
      }
    },
    [getToken, isLoaded, isSignedIn, jwtTemplateName]
  );

  return { data, loading, error, callApi };
}

export default useAuthenticatedApi;
