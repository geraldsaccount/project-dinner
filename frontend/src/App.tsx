import { useEffect } from "react";
import "./App.css";
import { useLogin } from "./hooks";

function App() {
  const { login } = useLogin();

  const loadUser = async () => {
    try {
      const response = await fetch("/api/auth");
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json(); // Assuming your API returns JSON
      console.log(data);
    } catch (error) {
      console.error("Failed to load user:", error);
    }
  };

  useEffect(() => {
    loadUser();
  }, []);

  return (
    <>
      <button onClick={() => login("github")}>Login</button>
    </>
  );
}

export default App;
