import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import * as ROUTES from "../constants/routes";
import UserContext from "../context/user";
import tokenParser from "../helpers/tokenParser";
import PlayerZone from "../components/game/zones/playerZone";
import CenterZone from "../components/game/zones/centerZone";
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
  const [players, setPlayers] = useState([]);

  const isSpectator = () =>
    !userToken || (user && user?.lobby?.game?.id !== parseInt(gameId));

  const fetchGame = async () => {
    try {
      const response = await axios.get(`/games/${gameId}`);
      const game = response.data;
      setGame(game);
      setPlayers(game.players);
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
    game && (
      <div className="bg-wood h-screen px-64 flex flex-col justify-center">
        {/* px-64 above is a temprary fix to prevent vertical overflow. TODO make this responsive */}
        {/* Top player names */}
        <div className="flex-none flex justify-between items-center py-4 text-white text-3xl">
          <p>{players[2] && players[2].user.username}</p>
          <p>{players[3] && players[3].user.username}</p>
        </div>
        {/* Game board (green part)*/}
        <div className="bg-felt rounded-3xl">
          <div className="p-2 grid grid-cols-5 gap-4">
            <div className="bg-red-400 row-span-2">
              {players[2] && <PlayerZone player={players[2]} />}
              {/* Top left */}
            </div>
            <div className="col-span-3 row-span-2">
              <CenterZone gameId={gameId} />
            </div>
            <div className="bg-green-400 row-span-2">
              {players[3] && <PlayerZone player={players[3]} />}
              {/* top right */}
            </div>
            <div className="bg-yellow-400 col-span-2 self-end">
              {players[0] && <PlayerZone player={players[0]} />}
              {/* bottom left */}
            </div>
            <div className="self-end">{/* Blank space */}</div>
            <div className="bg-purple-400 col-span-2 self-end items-end">
              {players[1] && <PlayerZone player={players[1]} reverse />}
              {/* bottom right */}
            </div>
          </div>
        </div>
        {/* Bottom player names */}
        <div className="flex-none flex justify-between items-center p-2 text-white text-3xl">
          <p>{players[0] && players[0].user.username}</p>
          <p>{players[1] && players[1].user.username}</p>
        </div>
      </div>
    )
  );
}
