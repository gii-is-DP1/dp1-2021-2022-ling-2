import { useState, useContext, useEffect } from "react";
import { Link, useHistory } from "react-router-dom";
import * as ROUTES from "../constants/routes";

export default function Login() {
  const history = useHistory(); // hook

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const [error, setError] = useState("");
  const handleLogin = async (event) => {
    event.preventDefault();

    try {
      history.push(ROUTES.HOME);
    } catch (error) {
      setUsername("");
      setPassword("");
      setError(error.message);
    }
  };

  useEffect(() => {
    document.title = "NTFH - Log In";
  });

  return <h1>Login page</h1>;
}
