import PropTypes from "prop-types";
import { useContext, useEffect, useState } from "react";
import { ListGroup } from "react-bootstrap";
import { useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import UserContext from "../context/user";
import tokenParser from "../helpers/tokenParser";

export default function Lobby() {
  // There should be some kind of listener
  // that listens for people to join. Maybe a GET
  // every some seconds or maybe a websocket.
  const [time, setTime] = useState(Date.now()); // Used to fetch lobby users every 2 seconds
  const [error, setError] = useState(null);
  const [lobby, setLobby] = useState(null); // current state of the lobby in the server. Updated perodically
  const { lobbyId } = useParams(); // TODO maybe we should just pass this as a param to the component
  const { userToken } = useContext(UserContext);
  const user = tokenParser(useContext(UserContext));
  useEffect(() => {
    // We have to notify the server we have joined the lobby
    async function notifyJoinLobby() {
      try {
        const payload = { username: user.username };
        const headers = { Authorization: "Bearer " + userToken };
        const response = await axios.post(`/lobbies/${lobbyId}/join`, payload, {
          headers,
        });
        console.log(response);
      } catch (error) {
        console.log(error);
      }
    }
    notifyJoinLobby();
  }, []); // Only run once

  useEffect(() => {
    const interval = setInterval(() => setTime(Date.now()), 1000);
    return () => {
      clearInterval(interval); // when the component is unmounted
    }; // Fetch the users in the lobby from the server to update the state and re-render if needed
  }, []); // Update "time" state every second

  useEffect(() => {
    async function fetchUsersInLobby() {
      try {
        const response = await axios.get(`/lobbies/${lobbyId}`);
        setLobby(response.data); // current status of the lobby (JSON as string)
      } catch (error) {
        setError(error);
      }
    }
    fetchUsersInLobby();
  }, [time]); // Since "time" state variable is updated every second, we can use it to fetch the lobby users periodically

  return (
    <>
      {lobby && (
        <>
          <h1>Lobby - {lobby.name}</h1>
          <div>Waiting for people to join</div>
          <div>Players in the lobby: {lobby.users.length}</div>
          <br />
          <ListGroup>
            {lobby.users.map((users, idx) => (
              // TODO fix, los nombres están bailongos (cambian de orden con el rerender)
              <ListGroup.Item key={idx}>{users.username}</ListGroup.Item>
            ))}
          </ListGroup>
        </>
      )}
      {/* TODO start game button */}
    </>
  );
}

Lobby.propTypes = {
  User: PropTypes.shape({
    username: PropTypes.string.isRequired,
  }),

  users: PropTypes.arrayOf(PropTypes.User),
};
