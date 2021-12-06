import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import * as ROUTES from "../constants/routes";
import UserContext from "../context/user";
import tokenParser from "../helpers/tokenParser";
import PlayerZoneHorizontal from "../components/game/zones/playerZoneHorizontal";
import PlayerZoneVertical from "../components/game/zones/playerZoneVertical";
import CenterZone from "../components/game/zones/centerZone";
/**
 *
 * @author andrsdt
 */
export default function Game() {
  const { gameId } = useParams(); // get params from react router link

  const history = useHistory();
  const { userToken } = useContext(UserContext);
  const loggedUser = tokenParser(useContext(UserContext));

  const [game, setGame] = useState(null);
  const [user, setUser] = useState(null);
  const [players, setPlayers] = useState([]);

  const isSpectator = () =>
    !userToken || (user && user?.lobby?.game?.id !== parseInt(gameId));

  const fetchGame = async () => {
    try {
      const response = await axios.get(`/games/${gameId}`);
      const _game = response.data;
      setGame(_game);
      setPlayers(_game.players);
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
    document.title = "NTFH - Game " + gameId;
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
      <div className="bg-wood h-screen px-16 flex flex-col justify-center">
        {/* Top player names */}
        <div className="flex-none flex justify-between items-center p-2 text-white text-3xl">
          <p>{players[2] && players[2].user.username}</p>
          <p>{players[3] && players[3].user.username}</p>
        </div>
        {/* Game board (felt part)*/}
        <div className="flex-1 bg-felt rounded-3xl">
          <div className="h-full p-2 grid grid-cols-5 gap-4">
            <div className="row-span-2">
              {players[2] && (
                <PlayerZoneVertical player={players[2]} rotation={90} />
              )}
              {/* Top left */}
            </div>
            <div className="col-span-3 row-span-2">
              <CenterZone game={game} />
            </div>
            <div className="row-span-2">
              {players[3] && (
                <PlayerZoneVertical
                  player={players[3]}
                  rotation={90}
                  counterclockwise
                />
              )}
              {/* top right */}
            </div>
            <div className="col-span-2 self-end max-w-xs 2xl:max-w-sm">
              {players[0] && <PlayerZoneHorizontal player={players[0]} />}
              {/* bottom left */}
            </div>
            <div className="self-end">{/* Blank space */}</div>
            <div className="col-span-2 self-end justify-self-end max-w-xs 2xl:max-w-sm">
              {players[1] && (
                <PlayerZoneHorizontal player={players[1]} reverse />
              )}
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
