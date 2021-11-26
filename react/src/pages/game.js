import { useContext } from "react";
import { useEffect, useState } from "react";
import { useParams, useHistory } from "react-router-dom";
import UserContext from "../context/user";
import ErrorContext from "../context/error";
import SpectatorToast from "../components/game/SpectatorToast";
import axios from "../api/axiosConfig";
import tokenParser from "../helpers/tokenParser";
import * as ROUTES from "../constants/routes";

export default function Game() {
  // route for /games/gameId
  const { gameId } = useParams(); // get params from react router link
  const { userToken } = useContext(UserContext);
  const loggedUser = tokenParser(useContext(UserContext));
  const [game, setGame] = useState(null);
  const [user, setUser] = useState(null);
  const { errors, setErrors } = useContext(ErrorContext); // Array of error objects
  const history = useHistory();

  const isSpectator = () =>
    !userToken || (user && user?.lobby?.game?.id !== parseInt(gameId));

  const fetchGame = async () => {
    try {
      const response = await axios.get(`/games/${gameId}`);
      setGame(response.data);
    } catch (error) {
      setErrors([...errors, error.response?.data]);
      if (error.response.status === 404) history.push(ROUTES.HOME);
    }
  };

  const fetchUser = async () => {
    try {
      const response = await axios.get(`/users/${loggedUser.username}`);
      setUser(response.data);
    } catch (error) {
      setErrors([...errors, error?.response?.data]);
    }
  };

  useEffect(() => {
    // fetch game info on page load
    fetchGame();
    if (!isSpectator()) fetchUser();
  }, []);

  return (
    <>
      {isSpectator() && <SpectatorToast />}
      <h1>This is game {gameId}</h1>;
    </>
  );
}
