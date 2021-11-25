import { useContext, useEffect, useState } from "react";
import { Button, Table } from "react-bootstrap";
import { Link, useHistory } from "react-router-dom";
import axios from "../api/axiosConfig";
import UserContext from "../context/user";
import Homebar from "../components/home/Homebar";
import * as ROUTES from "../constants/routes";
import errorContext from "../context/error";

export default function LobbyBrowser() {
  const history = useHistory(); // hook
  const { userToken } = useContext(UserContext);

  const { errors, setErrors } = useContext(errorContext); // Array of errors
  const [lobbyList, setLobbyList] = useState([]);

  const [gameCount, setGameCount] = useState(null);

  useEffect(() => {
    // get lobby list
    const fetchLobbies = async () => {
      try {
        const response = await axios.get(`lobbies`);
        setLobbyList(response.data);
      } catch (error) {
        setErrors([...errors, error.response]);
        history.push("/not-found");
      }
    };

    fetchLobbies();
  }, []);

  const refreshPage = () => {
    window.location.reload();
  };

  useEffect(() =>{
    const countGames = async () =>{
      try{
        const response = await axios.get(`games/count`);
        setGameCount(response.data);
      } catch (error) {
        setErrors([...errors, error.response]);
      }
    };

    countGames();
  }, []);

  const renderButtons = (lobby) => {
    if (!lobby.getHasStarted) {
      return getJoinStatus(lobby);
    } else {
      return getSpectateStatus(lobby);
    }
  }

  const getJoinStatus = (lobby) => {
    const fullLobbyFlag = lobby.users.length === lobby.maxPlayers;
    if (!fullLobbyFlag) {
      if (!(userToken === null)) {
        return (
          <Link to={ROUTES.LOBBY.replace(":lobbyId", lobby.id)}>
            <Button type="secondary" active>
              Join
            </Button>
          </Link>
        );
      } else {
        return (
          <Link to={ROUTES.LOGIN}>
            <Button type="secondary" active>
              Join
            </Button>
          </Link>
        );

      }
    } else {
      return (
        <Button type="submit" disabled>
          Join
        </Button>
      );
    }
  };

  const getSpectateStatus = (lobby) => {
    if (lobby.spectatorsAllowed) {
      return (
        <Link to={ROUTES.GAME.replace(":gameId", lobby.game.id)}>
          <Button type="secondary" active>
            Spectate
          </Button>
        </Link>
      );
    } else {
      return (
        <Button type="secondary" disabled>
          Spectate
        </Button>
      );
    }
  };


  return (
    <div>
      <div>
        <Homebar />
      </div>
      <h1>Lobby Browser</h1>
      <h3>Number of ongoing games: {gameCount}</h3>
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
              <th>{lobby.spectatorsAllowed ? "🟢" : "🔴"}</th>
              <th>
                {renderButtons(lobby)}
              </th>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
}
