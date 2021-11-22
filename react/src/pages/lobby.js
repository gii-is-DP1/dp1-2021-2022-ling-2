import PropTypes from "prop-types";
import { useContext, useEffect, useState } from "react";
import { ListGroup } from "react-bootstrap";
import { useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import UserContext from "../context/user";
import tokenParser from "../helpers/tokenParser";
import { useHistory } from "react-router-dom";
import * as ROUTES from "../constants/routes";
import Homebar from "../components/home/Homebar";
import Errors from "../components/common/Errors";

export default function Lobby() {
  // There should be some kind of listener
  // that listens for people to join. Maybe a GET
  // every some seconds or maybe a websocket.
  const [time, setTime] = useState(Date.now()); // Used to fetch lobby users every 2 seconds
  const [errors, setErrors] = useState([]);
  const [lobby, setLobby] = useState(null); // current state of the lobby in the server. Updated perodically
  const history = useHistory();
  const { lobbyId } = useParams(); // TODO maybe we should just pass this as a param to the component
  const { userToken } = useContext(UserContext);
  const user = tokenParser(useContext(UserContext));
  useEffect(() => {
    // We have to notify the server we have joined the lobby
    async function notifyJoinLobby() {
      try {
        const payload = { username: user.username };
        const headers = { Authorization: "Bearer " + userToken };
        await axios.post(`/lobbies/${lobbyId}/join`, payload, {
          headers,
        });
      } catch (error) {
        setErrors([...errors, error.message]);
      }
    }
    
    notifyJoinLobby();
  }, []); // Only run once

  useEffect(() => {
    document.title = "NTFH - Game lobby";
    if (!userToken) history.push(ROUTES.LOGIN); // Send the user to login screen if not logged in

    const interval = setInterval(() => setTime(Date.now()), 1000); // Useful later for fetching lobby users
    return () => {
      clearInterval(interval); // when the component is unmounted, clean up to prevent memory leaks
    };
  }, []); // Update "time" state every second

  useEffect(() => {
    // Fetch the lobby status from the server to update the state and re-render if needed
    async function fetchUsersInLobby() {
      try {
        const response = await axios.get(`/lobbies/${lobbyId}`);
        setLobby(response.data); // current status of the lobby (JSON as string)
      } catch (error) {
        setErrors([...errors, error.message]);
      }
    }
    fetchUsersInLobby();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [time]); // Since "time" state variable is updated every second, we can use it to fetch the lobby users periodically

  return (
    <>
      <div>
        <Homebar />
      </div>
      {lobby && (
        <>
          <h1>Lobby - {lobby.name}</h1>
          <Errors errors={errors} />
          <div>Waiting for people to join</div>
          <div>Players in the lobby: {lobby.users.length}</div>
          <br />
          <ListGroup>
            {lobby.users
              .sort((a, b) =>
                a.username < b.username ? -1 : a.username > b.username ? 1 : 0
              ) // arbitrary but consistent order
              .map((user, idx) => (
                <ListGroup.Item key={idx}>{user.username}</ListGroup.Item>
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
