import { useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { useHistory } from "react-router-dom";
import axios from "../api/axiosConfig";
import HomeButton from "../components/common/home-button";
import * as ROUTES from "../constants/routes";
import UserContext from "../context/user";
import { Lobby } from "../interfaces/Lobby";

/**
 *
 * @author andrsdt
 */
export default function LobbyBrowser() {
  const history = useHistory(); // hook
  const { userToken } = useContext(UserContext);

  const [lobbyList, setLobbyList] = useState<Lobby[]>([]);

  const fetchLobbies = async () => {
    try {
      const response = await axios.get(`lobbies`);
      setLobbyList(response.data);
    } catch (error: any) {
      toast.error(error?.message);
      history.push("/not-found");
    }
  };

  useEffect(() => {
    fetchLobbies();
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
                          onClick={fetchLobbies}
                        >
                          <p className={"text-xl text-gradient-ntfh"}>
                            Refresh
                          </p>
                        </button>
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-gray-900 divide-y divide-gray-200">
                    {lobbyList.map((lobby) => (
                      <tr key={lobby.id}>
                        <td className="text-table-td">
                          {lobby.users.length}/{lobby.maxPlayers}
                        </td>
                        <td className="text-table-td">{lobby.name}</td>
                        <td className="text-table-td">
                          {lobby.hasScenes ? "🟢" : "🔴"}
                        </td>
                        <td className="text-table-td">
                          {lobby.spectatorsAllowed ? "🟢" : "🔴"}
                        </td>
                        <td className="text-table-td">
                          {lobby.game ? ( // If the game has started
                            <button
                              className="btn-ntfh w-full bg-gray-800"
                              disabled={!lobby.spectatorsAllowed}
                              onClick={(e) =>
                                history.push(
                                  ROUTES.GAME.replace(
                                    ":gameId",
                                    lobby.game.id.toString()
                                  )
                                )
                              }
                            >
                              <p
                                className={`text-xl text-gradient-ntfh ${
                                  !lobby.spectatorsAllowed && "text-gray-500"
                                }`}
                              >
                                Spectate
                              </p>
                            </button>
                          ) : (
                            <button
                              className="btn-ntfh w-full bg-gray-800"
                              disabled={lobby.users.length === lobby.maxPlayers}
                              onClick={(e) => {
                                userToken
                                  ? history.push(
                                      ROUTES.LOBBY.replace(
                                        ":lobbyId",
                                        lobby.id.toString()
                                      )
                                    )
                                  : history.push(ROUTES.SIGNUP);
                              }}
                            >
                              <p
                                className={`text-xl text-gradient-ntfh ${
                                  lobby.users.length === lobby.maxPlayers &&
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
