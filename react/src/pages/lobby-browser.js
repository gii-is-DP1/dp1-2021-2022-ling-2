import axios from "../api/axiosConfig";
import { useEffect, useState } from "react";
import { Button, Table } from "react-bootstrap";
import { useHistory, Link } from "react-router-dom";
import Homebar from "../components/home/Homebar";
import * as ROUTES from "../constants/routes";

export default function LobbyBrowser() {
  const history = useHistory(); // hook

  const [lobbyList, setLobbyList] = useState([]);
  const [errors, setErrors] = useState([]);

  useEffect(() => {
    // get lobby list
    const fetchLobbies = async (e) => {
      e.preventDefault();
      try {
        const response = await axios.get(`lobbies`);
        setLobbyList(response.data);
      } catch (error) {
        history.push("/not-found");
        setErrors([...errors, error.message]);
      }
    };

    fetchLobbies();
  }, []);

  const refreshPage = () => {
    window.location.reload();
  };

  const getLobbyStatus = (lobby) => {
    const fullLobbyFlag = lobby.hasStarted || (lobby.users.length === lobby.maxPlayers);
    console.log(fullLobbyFlag);
    if(fullLobbyFlag && lobby.spectatorsAllowed) {
      return "Spectate";
    } else if(fullLobbyFlag) {
      return "";
    } else {
      return "Join";
    }
  };

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
            <th>
              <Button type="submit" onClick={() => refreshPage()}>
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
              <th>
                <Link to={ROUTES.LOBBY.replace(":lobbyId", lobby.id)}>
                  {getLobbyStatus(lobby)==="" ? "" : (
                    <Button type="submit">{getLobbyStatus(lobby)}</Button>
                  )}
                </Link>
              </th>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
}
