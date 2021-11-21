import { useContext } from "react";
import { Link } from "react-router-dom";
import UserContext from "../../context/user";
import { Button } from "react-bootstrap";
import tokenParser from "../../helpers/tokenParser";
import * as ROUTES from "../../constants/routes";
/**
 * Sidebar of a registered user. Will contain info about friends, etc.
 * @returns {React.Component}
 */
export default function Sidebar() {
  const { setUserToken } = useContext(UserContext);
  const user = tokenParser(useContext(UserContext));
  const username = "stockie";
  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
      <h1 className="text-white">Welcome back, {user.username}</h1>
      <Link to={ROUTES.PROFILE.replace(":username", username)}>
        <Button type="submit">
          Profile
        </Button>
      </Link>
      <Button type="submit" onClick={() => setUserToken(null)}>
        Log out
      </Button>
    </nav>
  );
}