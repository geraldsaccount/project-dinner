interface UseLoginResult {
  login: (provider: string) => void;
}

const useLogin = (): UseLoginResult => {
  const login = (provider: string) => {
    const host: string =
      window.location.host === "localhost:5173"
        ? "http://localhost:8080"
        : window.location.origin;

    window.open(`${host}/oauth2/authorization/${provider}`, "_self");
  };

  return { login };
};

export default useLogin;
