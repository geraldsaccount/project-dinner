import { useState, useCallback } from "react";

interface UseApiResult<TResponse, TBody> {
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
 * A generic hook for making unauthenticated API calls.
 *
 * @template TResponse The expected type of the response data.
 * @template TBody The expected type of the request body (for POST/PUT).
 * @returns {UseApiResult<TResponse, TBody>} An object containing data, loading state, error, and a callApi function.
 */
function useApi<TResponse, TBody = Record<string, unknown>>(): UseApiResult<
  TResponse,
  TBody
> {
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
      try {
        const headers: HeadersInit = {
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
    []
  );

  return { data, loading, error, callApi };
}

export default useApi;
