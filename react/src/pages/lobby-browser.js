import axios from "axios";
import { useEffect, useState } from "react";
import { Table } from "react-bootstrap";
import { useHistory } from "react-router-dom";
import Homebar from "../components/home/Homebar";


export default function LobbyBrowser() {
  const history = useHistory(); // hook

  const [lobbyList, setLobbyList] = useState([]);
  const [errors, setErrors] = useState([]);
  
  useEffect(() => {
    // get lobby list
    const fetchLobbies = async () => {
      try {
        const response = await axios.get(`lobbies`);
        setLobbyList(response.data);
      } catch (error) {
        history.push("/not-found");
        setErrors([...errors, error.message]);
      }
    }

    fetchLobbies();
  }, []);

  console.log(lobbyList);

  const lobbyRenderer = () => {

  };

  return (
    <div>
      <div>
        <Homebar />
      </div>
      <h1>Lobby Browser</h1>
      <h3>Implementation details in a comment in the code</h3>
      {/* This should be a table fetching all games (With spectate button) and all lobbies
      (with join button). Each row could be rendered via different components since they will
      have different columns (?), maybe even two tables... we will see, now it doesn't matter */}
      <Table bordered hover striped>
        <thead>
          <tr>
            <th>No.Players</th>
            <th>Game Name</th>
            <th>Scenes</th>
            <th>Spectators</th>
          </tr>
        </thead>
        <tbody>
          {lobbyList.forEach()}
        </tbody>
      </Table>
      <div id="gameList"></div>
    </div>
  );
}
