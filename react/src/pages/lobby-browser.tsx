import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory } from "react-router-dom";
import axios from "../api/axiosConfig";
import HomeButton from "../components/common/home-button";
import * as ROUTES from "../constants/routes";
import UserContext from "../context/user";
import { Game } from "../interfaces/Game";

/**
 *
 * @author andrsdt
 */
export default function LobbyBrowser() {
  const history = useHistory(); // hook
  const { userToken } = useContext(UserContext);

  const [gameList, setGameList] = useState<Game[]>([]);

  const fetchGames = async () => {
    let gameLs = [];
    try {
      const response = await axios.get(`games/lobby`);
      gameLs = response.data;
      setGameList(gameLs);
    } catch (error: any) {
      toast.error(error?.message);
    }
    try {
      const response = await axios.get(`games/ongoing`);
      gameLs = [...gameLs, ...response.data];
      setGameList(gameLs);
    } catch (error: any) {
      toast.error(error?.message);
    }
  };

  useEffect(() => {
    fetchGames();
    fetchGames();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const tableHeaders = ["No. Players", "Game Name", "Scenes", "Spectators"];

  return (
    <>
      <HomeButton />
      <div className="flex flex-col h-screen bg-wood p-8 items-center">
        <span className="text-center btn-ntfh mb-4">
          <p className="text-5xl text-gradient-ntfh">Lobby browser</p>
        </span>
        <div className="flex flex-col">
          <div className="overflow-x-auto">
            <div className="py-2 align-middle inline-block min-w-full">
              <div className="shadow overflow-hidden border-b border-gray-900 rounded-xl">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-800">
                    <tr>
                      {tableHeaders.map((header) => (
                        <th key={header} scope="col" className="text-table-th">
                          {header}
                        </th>
                      ))}
                      <th scope="col" className="text-table-th">
                        <button
                          className="btn-ntfh bg-gray-900 w-full"
                          onClick={fetchGames}
                        >
                          <p className={"text-xl text-gradient-ntfh"}>
                            Refresh
                          </p>
                        </button>
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-gray-900 divide-y divide-gray-200">
                    {gameList.map((game) => (
                      <tr key={game.id}>
                        <td className="text-table-td">
                          {game.players.length}/{game.maxPlayers}
                        </td>
                        <td className="text-table-td">{game.name}</td>
                        <td className="text-table-td">
                          {game.hasScenes ? "🟢" : "🔴"}
                        </td>
                        <td className="text-table-td">
                          {game.spectatorsAllowed ? "🟢" : "🔴"}
                        </td>
                        <td className="text-table-td">
                          {game.hasStarted ? (
                            <button
                              className="btn-ntfh w-full bg-gray-800"
                              disabled={!game.spectatorsAllowed}
                              onClick={(e) =>
                                history.push(
                                  ROUTES.GAME.replace(
                                    ":gameId",
                                    game.id.toString()
                                  )
                                )
                              }
                            >
                              <p
                                className={`text-xl text-gradient-ntfh ${
                                  !game.spectatorsAllowed && "text-gray-500"
                                }`}
                              >
                                Spectate
                              </p>
                            </button>
                          ) : (
                            <button
                              className="btn-ntfh w-full bg-gray-800"
                              disabled={game.players.length === game.maxPlayers}
                              onClick={(e) => {
                                userToken
                                  ? history.push(
                                      ROUTES.GAME.replace(
                                        ":gameId",
                                        game.id.toString()
                                      )
                                    )
                                  : history.push(ROUTES.SIGNUP);
                              }}
                            >
                              <p
                                className={`text-xl text-gradient-ntfh ${
                                  game.players.length === game.maxPlayers &&
                                  "text-gray-500"
                                }`}
                              >
                                Join
                              </p>
                            </button>
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
