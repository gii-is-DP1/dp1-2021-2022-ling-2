import * as ROUTES from "../constants/routes";
import { useEffect } from "react";
import { Link } from "react-router-dom";

export default function Home() {
  useEffect(() => {
    document.title = "No Time for Heroes";
  });
  return (
    <div className="home">
      <h1>Home</h1>
      <p>
        <Link to={ROUTES.SIGNUP}>Sign up</Link>
      </p>
      <Link to={ROUTES.LOGIN}>
        <p>Log in</p>
      </Link>
    </div>
  );
}
