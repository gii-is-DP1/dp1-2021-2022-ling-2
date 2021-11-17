import { useEffect } from "react";

export default function Login() {
  useEffect(() => {
    document.title = "NTFH - Log In";
  });
  return <h1>Login page</h1>;
}
