import { useContext, useEffect, useState } from "react";
import { Button, Table } from "react-bootstrap";
import { useHistory } from "react-router-dom";
import axios from "../api/axiosConfig";
import UserContext from "../context/user";
import Homebar from "../components/home/Homebar";
import * as ROUTES from "../constants/routes";
import toast from "react-hot-toast";

export default function LobbyBrowser() {
  const history = useHistory(); // hook
  const { userToken } = useContext(UserContext);

  const [lobbyList, setLobbyList] = useState([]);

  const fetchLobbies = async () => {
    try {
      const response = await axios.get(`lobbies`);
      setLobbyList(response.data);
    } catch (error) {
      toast.error(error.response?.data);
      history.push("/not-found");
    }
  };

  useEffect(() => {
    fetchLobbies();
  }, []);

  return (
    <div>
      <div>
        <Homebar />
      </div>
      <h1>Lobby Browser</h1>
      {/* This should be a table fetching all games (With spectate button) and all lobbies
      (with join button). Each row could be rendered via different components since they will
      have different columns (?), maybe even two tables... we will see, now it doesn't matter */}
      <Table bordered hover striped>
        {/* TODO: Eventually turn this into a proper table or flex container */}
        <thead>
          <tr>
            <th>No.Players</th>
            <th>Game Name</th>
            <th>Scenes</th>
            <th>Spectators</th>
            <th>
              <Button type="submit" onClick={fetchLobbies}>
                ↻
              </Button>
            </th>
          </tr>
        </thead>
        <tbody>
          {lobbyList.map((lobby, idx) => (
            <tr>
              <th>
                {lobby.users.length}/{lobby.maxPlayers}
              </th>
              <th>{lobby.name}</th>
              <th>{lobby.hasScenes ? "🟢" : "🔴"}</th>
              <th>{lobby.spectatorsAllowed ? "🟢" : "🔴"}</th>
              <th>
                {lobby.game ? ( // If the game has started
                  <Button
                    // type="secondary"
                    disabled={!lobby.spectatorsAllowed}
                    onClick={(e) =>
                      history.push(
                        ROUTES.GAME.replace(":gameId", lobby.game.id)
                      )
                    }
                  >
                    Spectate
                  </Button>
                ) : (
                  <Button
                    type="secondary"
                    disabled={lobby.users.length === lobby.maxPlayers}
                    onClick={(e) => {
                      userToken
                        ? history.push(
                            ROUTES.LOBBY.replace(":lobbyId", lobby.id)
                          )
                        : history.push(ROUTES.SIGNUP);
                    }}
                  >
                    Join
                  </Button>
                )}
              </th>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
}
