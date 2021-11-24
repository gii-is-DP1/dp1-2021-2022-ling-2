import { useContext } from "react";
import { Button } from "react-bootstrap";
import { Link, useHistory } from "react-router-dom";
import * as ROUTES from "../../constants/routes";
import UserContext from "../../context/user";
import tokenParser from "../../helpers/tokenParser";
/**
 * Sidebar of a registered user. Will contain info about friends, etc.
 * @returns {React.Component}
 */
export default function Sidebar() {
  const history = useHistory();
  const { setUserToken } = useContext(UserContext);
  const user = tokenParser(useContext(UserContext));

  const handleLogout = () => {
    setUserToken(null);
    history.push(ROUTES.HOME);
  };

  const handleShare = () => {
    const message =
      "Hey, check out this web app for playing No Time for Heroes! " +
      window.location.href;
    // copy to clipboard
    navigator.clipboard.writeText(message);
    // show success message
    alert("Copied to clipboard!");
    // TODO when the "Errors" component also supports information
    // messages, show it that way instead
  };

  return (
    <nav className="navbar navbar-dark bg-dark">
      <h1 className="text-white">Welcome back, {user.username}</h1>
      <span>
        <Link to={ROUTES.PROFILE.replace(":username", user.username)}>
          <Button className="m-2" type="submit">
            Profile
          </Button>
        </Link>
        <Button className="m-2" type="submit" onClick={handleLogout}>
          Log out
        </Button>
        <Button className="m-2" type="submit" onClick={handleShare}>
          Share the game
        </Button>
      </span>
    </nav>
  );
}
