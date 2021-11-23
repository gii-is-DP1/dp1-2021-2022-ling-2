import PropTypes from "prop-types";
import { useContext, useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import Errors from "../components/common/Errors";
import Homebar from "../components/home/Homebar";
import UsersInLobby from "../components/lobby/UsersInLobby";
import * as ROUTES from "../constants/routes";
import UserContext from "../context/user";
import tokenParser from "../helpers/tokenParser";

export default function Lobby() {
  const REFRESH_RATE = 1000; // fetch lobby status every 1000 miliseconds
  const [time, setTime] = useState(Date.now()); // Used to fetch lobby users every 2 seconds
  const [errors, setErrors] = useState([]);
  const [lobby, setLobby] = useState(null); // current state of the lobby in the server. Updated perodically
  const history = useHistory();
  const { lobbyId } = useParams(); // TODO maybe we should just pass this as a param to the component
  const { userToken } = useContext(UserContext);
  const user = tokenParser(useContext(UserContext));

  async function fetchLobbyStatus() {
    try {
      const response = await axios.get(`/lobbies/${lobbyId}`);
      const newLobby = response.data;
      if (lobby && userInLobby(user, lobby) && !userInLobby(user, newLobby))
        // if I was in the list of the previous lobby and not, I was kicked. Send me to browse lobbies
        history.push(ROUTES.BROWSE_LOBBIES);
      setLobby(newLobby);
      return newLobby;
    } catch (error) {
      if (!error.response.data) {
        history.push(ROUTES.BROWSE_LOBBIES);
        return;
      }
      setErrors([...errors, error.message]);
    }
  }

  async function notifyJoinLobby() {
    try {
      const payload = { username: user.username };
      const headers = { Authorization: "Bearer " + userToken };
      await axios.post(`/lobbies/${lobbyId}/join`, payload, {
        headers,
      });
    } catch (error) {
      if (error?.response?.status === 404) history.push(ROUTES.BROWSE_LOBBIES);
      setErrors([...errors, error.message]);
    }
  }

  async function handleRemoveUserFromLobby(username) {
    try {
      // axios.delete only has 2 parameters, url and headers)
      await axios.delete(`/lobbies/${lobby.id}/remove/${username}`, {
        headers: { Authorization: "Bearer " + userToken },
      });
    } catch (error) {
      setErrors([...errors, error.message]);
    }
  }

  const userInLobby = (_user, _lobby) =>
    _lobby.users.map((u) => u.username).includes(_user.username);

  useEffect(() => {
    // We have to notify the server we have joined the lobby
    document.title = "NTFH - Game lobby";
    if (!userToken) history.push(ROUTES.LOGIN); // Send the user to login screen if not logged in

    async function firstFetch() {
      const lobby = await fetchLobbyStatus();
      // will be only executed if the user is not in the lobby yet
      if (!userInLobby(user, lobby)) notifyJoinLobby();
    }
    firstFetch();
  }, []); // Only run once

  useEffect(() => {
    // TODO extract timer to hook
    const interval = setInterval(() => setTime(Date.now()), REFRESH_RATE); // Useful later for fetching lobby users
    return () => {
      clearInterval(interval); // when the component is unmounted, clean up to prevent memory leaks
    };
  }, []); // Update "time" state every second

  useEffect(() => {
    // Fetch the lobby status from the server to update the state and re-render if needed
    fetchLobbyStatus();
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
          <Button onClick={(e) => handleRemoveUserFromLobby(user.username)}>
            {lobby.host.username === user.username ? "Delete" : "Leave"} lobby
          </Button>
          <div>Waiting for people to join</div>
          <div>Players in the lobby: {lobby.users.length}</div>
          <br />
          <UsersInLobby
            lobby={lobby}
            handleRemoveUserFromLobby={handleRemoveUserFromLobby}
          />
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
