import { useEffect } from "react";
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
          You have been assigned a guest account:{" "}
          {JSON.parse(unregisteredUser).username}. If you want to access all the
          functionalities, don't forget to{" "}
          <Link to={ROUTES.SIGNUP}>Sign up</Link> or{" "}
          <Link to={ROUTES.LOGIN}>Log in</Link>
        </p>
      )}
    </span>
  );
}
