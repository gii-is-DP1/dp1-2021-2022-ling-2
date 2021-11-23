import { useContext, useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import axios from "../api/axiosConfig";
import Errors from "../components/common/Errors";
import Sidebar from "../components/home/Sidebar";
import UnregisteredSidebar from "../components/home/UnregisteredSidebar";
import * as ROUTES from "../constants/routes";
import UnregisteredUserContext from "../context/unregisteredUser";
import UserContext from "../context/user";
import tokenParser from "../helpers/tokenParser";
// import "../resources/css/nord.css";

export default function Home() {
  const { userToken } = useContext(UserContext);
  const user = tokenParser(useContext(UserContext));
  const { unregisteredUser, setUnregisteredUser } = useContext(
    UnregisteredUserContext
  );

  const [errors, setErrors] = useState([]);

  const isAdmin = (_user) => _user.authorities.includes("admin");

  useEffect(() => {
    // make this execute only once
    if (!unregisteredUser) {
      // if there aren't unregistered user credentials, ask for some
      async function fetchData() {
        try {
          const response = await axios.get("/unregistered-users");
          setUnregisteredUser(response.data);
        } catch (error) {
          setErrors([...errors, error.message]);
        }
      }
      fetchData();
    }
  }, [unregisteredUser, setUnregisteredUser]);

  useEffect(() => {
    document.title = "No Time for Heroes";
  });

  return (
    <span>
      <h1>Home</h1>
      <Errors errors={errors} />
      {userToken ? (
        <>
          <Sidebar />
          <Link to={ROUTES.CREATE_LOBBY}>
            <Button type="submit">Create game</Button>
          </Link>
        </>
      ) : (
        <UnregisteredSidebar />
      )}
      <Link to={ROUTES.BROWSE_LOBBIES}>
        <Button type="submit">Browse games</Button>
      </Link>
      {user && isAdmin(user) ? (
        <Link to={ROUTES.ADMIN_PAGE}>
          <Button type="submit">Admin Page</Button>
        </Link>
      ) : (
        ""
      )}
    </span>
  );
}
