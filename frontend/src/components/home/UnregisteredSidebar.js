import { useContext } from "react";
import { Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import * as ROUTES from "../../constants/routes";
import UnregisteredUserContext from "../../context/unregisteredUser";

export default function UnregisteredSidebar() {
  const { unregisteredUser } = useContext(UnregisteredUserContext);

  // The view will be updated every time a state variable changes
  return (
    <span>
      <h1>Welcome to No Time for Heroes!</h1>
      {!unregisteredUser ? (
        <p>Loading...</p>
      ) : (
        <p>
          Hello <b>{unregisteredUser.username}</b>! You have been assigned a
          guest account. If you want to access all the functionalities of the
          game, please <br></br>
          <Link to={ROUTES.SIGNUP}>
            <Button variant="primary">Sign up</Button>
          </Link>{" "}
          <Link to={ROUTES.LOGIN}>
            <Button variant="primary">Log In</Button>
          </Link>{" "}
        </p>
      )}
    </span>
  );
}
