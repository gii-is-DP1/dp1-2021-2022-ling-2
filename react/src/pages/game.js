import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import * as ROUTES from "../constants/routes";
import UserContext from "../context/user";
import tokenParser from "../helpers/tokenParser";

/**
 *
 * @author andrsdt
 */
export default function Game() {
  // route for /games/gameId
  const { gameId } = useParams(); // get params from react router link
  const { userToken } = useContext(UserContext);
  const loggedUser = tokenParser(useContext(UserContext));
  const [game, setGame] = useState(null);
  const [user, setUser] = useState(null);
  const history = useHistory();

  const isSpectator = () =>
    !userToken || (user && user?.lobby?.game?.id !== parseInt(gameId));

  const fetchGame = async () => {
    try {
      const response = await axios.get(`/games/${gameId}`);
      setGame(response.data);
    } catch (error) {
      toast.error(error.response?.data?.message);
      if (error.response?.status === 404) history.push(ROUTES.HOME);
    }
  };

  const fetchUser = async () => {
    try {
      const response = await axios.get(`/users/${loggedUser.username}`);
      setUser(response.data);
    } catch (error) {
      toast.error(error.response?.data?.message);
    }
  };

  useEffect(() => {
    // fetch game info on page load
    fetchGame();
    if (!isSpectator()) {
      fetchUser();
    } else {
      // if user is spectator, render a toast
      toast("Spectator", {
        position: "top-right",
        duration: Infinity,
        icon: "👁️",
        id: "Spectator",
      });
    }
  }, []);

  return (
    <div className="p-3 grid grid-cols-5 gap-4 h-screen bg-table">
      <h1 className="bg-red-400 row-span-2">title 1</h1>
      <h1 className="bg-red-400 col-span-3 row-span-2">title 2</h1>
      <h1 className="bg-red-400 row-span-2">title 3</h1>
      <h1 className="bg-red-400 col-span-2">title 4</h1>
      <h1>{/* Blank space */}</h1>
      <h1 className="bg-red-400 col-span-2">title 7</h1>
    </div>
  );
}
