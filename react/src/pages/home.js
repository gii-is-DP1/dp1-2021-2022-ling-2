import { useContext, useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import { Link, useHistory } from "react-router-dom";
import axios from "../api/axiosConfig";
import Sidebar from "../components/home/Sidebar";
import UnregisteredSidebar from "../components/home/UnregisteredSidebar";
import * as ROUTES from "../constants/routes";
import ErrorContext from "../context/error";
import UnregisteredUserContext from "../context/unregisteredUser";
import UserContext from "../context/user";
import hasAuthority from "../helpers/hasAuthority";
import tokenParser from "../helpers/tokenParser";

export default function Home() {
  const { errors, setErrors } = useContext(ErrorContext); // hook
  const { userToken } = useContext(UserContext);
  const user = tokenParser(useContext(UserContext));
  const [currentUser, setCurrentUser] = useState();
  const history = useHistory(); // hook
  const { unregisteredUser, setUnregisteredUser } = useContext(
    UnregisteredUserContext
  );

  async function fetchUnregisteredUserData() {
    try {
      const response = await axios.get("/unregistered-users");
      setUnregisteredUser(response.data);
    } catch (error) {
      setErrors([...errors, error.response?.data]);
    }
  }

  async function fetchUserData() {
    try {
      const response = await axios.get(`/users/${user.username}`);
      setCurrentUser(response.data);
    } catch (error) {
      setErrors([...errors, error.response?.data]);
    }
  }

  const userInLobby = () => currentUser?.lobby;

  const userInGame = () => currentUser?.lobby?.game;

  useEffect(() => {
    document.title = "No Time for Heroes";
  }, []);

  useEffect(() => {
    // Unregistered user creation
    if (!unregisteredUser) {
      // if there aren't unregistered user credentials, ask for some
      fetchUnregisteredUserData();
    }
    if (userToken) {
      // if there are user credentials, fetch his info
      fetchUserData();
    }
  }, []);

  return (
    <span>
      <h1>Home</h1>

      {/* TODO Rework all buttons, ask James what the fuck he means */}
      {userToken ? <Sidebar /> : <UnregisteredSidebar />}

      {/* Buttons */}
      {user && !userInLobby() && (
        <>
          <Link to={ROUTES.BROWSE_LOBBIES}>
            <Button type="submit">Browse Games</Button>
          </Link>
          <Link to={ROUTES.CREATE_LOBBY}>
            <Button type="submit">Create lobby</Button>
          </Link>
        </>
      )}
      {userInLobby() && !userInGame() && (
        <Link to={ROUTES.LOBBY.replace(":lobbyId", currentUser?.lobby?.id)}>
          <Button type="submit">Rejoin Lobby</Button>
        </Link>
      )}
      {userInGame() && (
        <Link to={ROUTES.GAME.replace(":gameId", currentUser?.lobby?.game?.id)}>
          <Button type="submit">Rejoin Game</Button>
        </Link>
      )}
      {hasAuthority(user, "admin") && (
        <Link to={ROUTES.ADMIN_PAGE}>
          <Button type="submit">Admin Page</Button>
        </Link>
      )}
      <Link to={ROUTES.STATISTICS}>
        <Button type="submit">Statistics</Button>
      </Link>
    </span>
  );
}
