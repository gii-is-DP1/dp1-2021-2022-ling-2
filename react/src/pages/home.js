import { useContext, useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import { Link, useHistory } from "react-router-dom";
import axios from "../api/axiosConfig";
import Sidebar from "../components/home/Sidebar";
import UnregisteredSidebar from "../components/home/UnregisteredSidebar";
import * as ROUTES from "../constants/routes";
import UnregisteredUserContext from "../context/unregisteredUser";
import UserContext from "../context/user";
import hasAuthority from "../helpers/hasAuthority";
import tokenParser from "../helpers/tokenParser";
import ErrorContext from "../context/error";

// import "../resources/css/nord.css";

export default function Home() {
  const { errors, setErrors } = useContext(ErrorContext); // hook
  const { userToken } = useContext(UserContext);
  const user = tokenParser(useContext(UserContext));
  const [currentUser, setCurrentUser] = useState();
  const history = useHistory(); // hook
  const { unregisteredUser, setUnregisteredUser } = useContext(
    UnregisteredUserContext
  );

  const isAdmin = (_user) => _user.authorities.includes("admin");

  useEffect(() => {
    document.title = "No Time for Heroes";
  }, []);

  useEffect(() => {
    // Unregistered user creation
    if (!unregisteredUser) {
      // if there aren't unregistered user credentials, ask for some
      async function fetchData() {
        try {
          const response = await axios.get("/unregistered-users");
          setUnregisteredUser(response.data);
        } catch (error) {
          setErrors([...errors, error.response.data]);
        }
      }
      fetchData();
    }
  }, [unregisteredUser, setUnregisteredUser]);

  const generateUserButtons = () => {

    // TODO acutal rejoin game
    //if (currentUser.lobby === null) {
    if (true) {
      return (
        <>
          <Link to={ROUTES.CREATE_LOBBY}>
            <Button type="submit">Create Game</Button>
          </Link>
          <Link to={ROUTES.BROWSE_LOBBIES}>
            <Button type="submit">Browse Games</Button>
          </Link>
        </>
      );
    } else {
      <Button type="submit">Rejoin Game</Button>
    }
  };

  return (
    <span>
      <h1>Home</h1>

      {/* TODO Rework all buttons, ask James what the fuck he means */}
      {userToken ? <Sidebar /> : <UnregisteredSidebar />}

      {/* Buttons */}
      {!userToken ? (
        <Link to={ROUTES.BROWSE_LOBBIES}>
          <Button type="submit">Browse Games</Button>
        </Link>) : generateUserButtons()
      }

      {/* Admin Page*/}
      {user && hasAuthority(user, "admin") ? (
        <Link to={ROUTES.ADMIN_PAGE}>
          <Button type="submit">Admin Page</Button>
        </Link>
      ) : ""}
    </span >
  );
}
