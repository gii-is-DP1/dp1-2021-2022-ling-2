import { useEffect } from "react";
import { Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import * as ROUTES from "../../constants/routes";
import * as PATHS from "../../constants/paths";
import { useLocalStorage } from "../../hooks/useLocalStorage";
import useAxios from "axios-hooks";

export default function UnregisteredSidebar() {
  const [{ data, loading, error }, generateUser] = useAxios(
    {
      url: `${PATHS.API_BASE_URL}/unregistered-users`,
    },
    { manual: true }
  );

  const [unregisteredUser, setUnregisteredUser] = useLocalStorage(
    "unregisteredUser",
    ""
  );

  useEffect(() => {
    if (unregisteredUser === "") generateUser();
  }, [unregisteredUser, generateUser]);

  useEffect(() => {
    if (data) {
      setUnregisteredUser(JSON.stringify(data));
    }
  }, [data, setUnregisteredUser]);

  // The view will be updated every time a state variable changes
  return (
    <span>
      <h1>Welcome to No Time for Heroes!</h1>
      {unregisteredUser === "" ? (
        <p>Loading...</p>
      ) : (
        <p>
          Hello <b>{JSON.parse(unregisteredUser).username}</b>! You have been
          assigned a guest account. If you want to access all the
          functionalities of the game, please{" "}
          <br></br>
          <Link to={ROUTES.SIGNUP}>
            <Button variant="primary">Sign up</Button>
          </Link>{' '}
          <Link to={ROUTES.LOGIN}>
            <Button variant="primary">Log In</Button>
          </Link>{' '}
        </p>
      )}
    </span>
  );
}
