import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import * as ROUTES from "../constants/routes";

export default function SignUp() {
  useEffect(() => {
    document.title = "NTFH - Sign up";
  });

  const [username, setUsername] = useState("");
  const [emailAddress, setEmailAddress] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  return (
    <div>
      <h1>Sign up page</h1>
      <p>
        {" "}
        Have an account?
        <Link to={ROUTES.LOGIN}>Log in</Link>
      </p>
    </div>
  );
}
