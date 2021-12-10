import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import CenterZone from "../components/game/zones/centerZone";
import PlayerZoneHorizontal from "../components/game/zones/playerZoneHorizontal";
import PlayerZoneVertical from "../components/game/zones/playerZoneVertical";
import * as ROUTES from "../constants/routes";
import UserContext from "../context/user";
import tokenParser from "../helpers/tokenParser";
import { Game as IGame } from "../interfaces/Game";
import { Player } from "../interfaces/Player";
import { User } from "../interfaces/User";

/**
 *
 * @author andrsdt
 */
export default function Game() {
  const history = useHistory();
  const loggedUser = tokenParser(useContext(UserContext));

  const { gameId } = useParams<{ gameId: string }>(); // get params from react router link
  const [game, setGame] = useState<IGame | null>(null);
  const [user, setUser] = useState<User | null>(null);
  const [players, setPlayers] = useState<Player[]>([]);

  const isSpectator = (_user: User | null) =>
    !loggedUser.username ||
    (_user && _user?.lobby?.game?.id !== parseInt(gameId));

  const playersInRenderOrder = (_players: Player[]) => {
    const orderedPlayerList: Player[] = _players.sort(
      (p1, p2) => p1.turnOrder - p2.turnOrder
    );
    /* In case we are someone playing the game and not a spectator, make sure
     * that the first player of the list (the one who will be rendered on the
     * bottom left part) is us
     * */
    if (user && !isSpectator(user)) {
      // Rotate the list until the current player is at the first position,
      // but they still keep the same order (by rotating values in array)
      while (orderedPlayerList[0].user.username !== loggedUser.username) {
        const firstElement = orderedPlayerList.shift(); // removes it form array
        if (firstElement) orderedPlayerList.push(firstElement); // adds it at the end
      }
    }
    return orderedPlayerList;
  };

  const fetchGame = async () => {
    try {
      const response = await axios.get(`/games/${gameId}`);
      const _game = response.data;
      setGame(_game);
      const sortedPlayers = playersInRenderOrder(_game.players);
      setPlayers(sortedPlayers);
    } catch (error: any) {
      toast.error(error.response?.data?.message);
      if (error.response?.status === 404) history.push(ROUTES.HOME);
    }
  };

  const fetchUser = async () => {
    try {
      const response = await axios.get(`/users/${loggedUser.username}`);
      setUser(response.data);
    } catch (error: any) {
      toast.error(error.response?.data?.message);
    }
  };

  useEffect(() => {
    document.title = "NTFH - Game " + gameId;
    loggedUser.username && fetchUser();
    return function cleanup() {
      toast.dismiss("Spectator");
      // To avoid sending the user to the lobby, that
      // would redirect him/her to the game again
      history.replace(ROUTES.HOME);
    };
  }, []);

  useEffect(() => {
    fetchGame();
    if (isSpectator(user)) {
      // if user is spectator, render a toast
      toast("Spectator", {
        position: "top-center",
        duration: Infinity,
        icon: "👁️",
        id: "Spectator",
      });
    }
  }, [user]);

  return (
    game && (
      <div className="bg-wood h-screen px-16 flex flex-col justify-center">
        {/* Top player names */}
        <div className="flex-none flex justify-between items-center p-2 text-white text-3xl">
          <p>{players[3] && players[3].user.username}</p>
          <p>{players[2] && players[2].user.username}</p>
        </div>
        {/* Game board (felt part)*/}
        <div className="flex-1 bg-felt rounded-3xl">
          <div className="h-full p-2 grid grid-cols-5 gap-4">
            <div className="row-span-2">
              {players[2] && (
                <PlayerZoneVertical player={players[3]} rotation={90} />
              )}
              {/* Top left */}
            </div>
            <div className="col-span-3 row-span-2">
              <CenterZone game={game} />
            </div>
            <div className="row-span-2">
              {players[3] && (
                <PlayerZoneVertical
                  player={players[2]}
                  rotation={90}
                  counterclockwise
                />
              )}
              {/* Top right */}
            </div>
            <div className="col-span-2 self-end max-w-xs 2xl:max-w-sm">
              {players[0] && <PlayerZoneHorizontal player={players[0]} />}
              {/* Bottom left (My hand) */}
            </div>
            <div className="self-end">{/* Blank space */}</div>
            <div className="col-span-2 self-end justify-self-end max-w-xs 2xl:max-w-sm">
              {players[1] && (
                <PlayerZoneHorizontal player={players[1]} reverse />
              )}
              {/* Bottom right */}
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