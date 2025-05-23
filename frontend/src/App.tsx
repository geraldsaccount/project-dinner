import { useEffect } from "react";
import "./App.css";
import { useLogin } from "./hooks";
import axios from "axios";

function App() {
  const { login } = useLogin();

  const loadUser = async () => {
    const response = await axios.get<string>("/api/auth");
    console.log(response.data);
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
