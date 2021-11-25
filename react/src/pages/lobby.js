import PropTypes from "prop-types";
import { useContext, useEffect, useState } from "react";
import { Button, Col, Form, Row } from "react-bootstrap";
import { useHistory, useParams } from "react-router-dom";
import axios from "../api/axiosConfig";
import Homebar from "../components/home/Homebar";
import UsersInLobby from "../components/lobby/UsersInLobby";
import * as ROUTES from "../constants/routes";
import ErrorContext from "../context/error";
import UserContext from "../context/user";
import tokenParser from "../helpers/tokenParser";

export default function Lobby() {
  const REFRESH_RATE = 1000; // fetch lobby status every 1000 miliseconds

  const { errors, setErrors } = useContext(ErrorContext); // Array of error objects
  const [time, setTime] = useState(Date.now()); // Used to fetch lobby users every 2 seconds
  const [lobby, setLobby] = useState(null); // current state of the lobby in the server. Updated perodically
  const history = useHistory();
  const { lobbyId } = useParams(); // TODO maybe we should just pass this as a param to the component
  const { userToken } = useContext(UserContext);
  const user = tokenParser(useContext(UserContext));
  const [character, setCharacter] = useState("none");
  const [gender, setGender] = useState("male");
  const [fullLobby, setFullLobby] = useState(false);
  const [charactersTaken, setCharactersTaken] = useState([]);

  const characters = ["ranger", "rogue", "warrior", "wizard"];
  const genders = ["male", "female"];

  const getCharacterId = () => {
    if (character === "none") return null;
    return 1 + 2 * characters.indexOf(character) + genders.indexOf(gender);
    // Input: warrior, female
    // Output: 3+ 1  = 4 (id Of female warrior in the DB is 4)
    // This is a temporal solution to be refactored in the future
  };

  async function fetchLobbyStatus() {
    try {
      const response = await axios.get(`/lobbies/${lobbyId}`);
      const newLobby = response.data;
      if (lobby && !userInLobby(user, newLobby)) {
        // if I was in the list of the previous lobby and not, I was kicked. Send me to browse lobbies
        history.push(ROUTES.BROWSE_LOBBIES);
        return;
      }
      if (lobby && lobby.game) {
        // If the lobby I am trying to join has already started, redirect me to the game
        history.push(ROUTES.GAME.replace(":gameId", lobby.game.id));
        return;
      }
      setLobby(newLobby);
      setFullLobby(newLobby.maxPlayers === newLobby.users.length);
      const takenCharacters = newLobby.users.map((_user) =>
        _user.character?.characterTypeEnum?.toLowerCase()
      );
      setCharactersTaken(takenCharacters);
      return newLobby;
    } catch (error) {
      // TODO: Throw NotFoundError on the backend with the message "this lobby does not exist anymore"
      setErrors([...errors, error.response.data]);
      history.push(ROUTES.BROWSE_LOBBIES);
      return;
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
      setErrors([...errors, error.response.data]);
      if (error?.response?.status === 404) history.push(ROUTES.BROWSE_LOBBIES);
    }
  }

  async function handleRemoveUserFromLobby(username) {
    try {
      // axios.delete only has 2 parameters, url and headers)
      await axios.delete(`/lobbies/${lobby.id}/remove/${username}`, {
        headers: { Authorization: "Bearer " + userToken },
      });
      if (username === lobby.host.username) history.push(ROUTES.BROWSE_LOBBIES);
    } catch (error) {
      setErrors([...errors, error.response.data]);
    }
  }

  const userInLobby = (_user, _lobby) =>
    _lobby.users.map((u) => u.username).includes(_user.username);

  const createGame = async (e) => {
    e.preventDefault();
    try {
      const payload = lobby;
      const response = await axios.post("/games", payload, {
        headers: { Authorization: "Bearer " + userToken },
      });
      const gameId = response.data.gameId;
      history.push(ROUTES.GAME.replace(":gameId", gameId));
    } catch (error) {
      setErrors([...errors, error.response.data]);
    }
  };

  useEffect(() => {
    // We have to notify the server we have joined the lobby
    document.title = "NTFH - Game lobby";

    async function firstFetch() {
      const lobby = await fetchLobbyStatus();
      // will be only executed if the user is not in the lobby yet
      if (lobby && !userInLobby(user, lobby)) notifyJoinLobby();
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

  useEffect(() => {
    async function updateUserCharacter() {
      try {
        user.character = getCharacterId();
        const payload = { ...user };
        const response = await axios.put(`/users`, payload, {
          headers: { Authorization: "Bearer " + userToken },
        });
      } catch (error) {
        setErrors([...errors, error.response.data]);
      }
    }
    updateUserCharacter();
  }, [character, gender]);

  return (
    <>
      <Homebar />
      {lobby && (
        <>
          <Row>
            <Col>
              <h1>Lobby - {lobby.name}</h1>
              {/* TODO separate method for deleting game. Currently it's handled in the
               user removal endpoint, it should have its own endpoint and Axios call */}
              <Button onClick={(e) => handleRemoveUserFromLobby(user.username)}>
                {lobby.host.username === user.username ? "Delete" : "Leave"}{" "}
                lobby
              </Button>
              <div>
                {fullLobby ? "The room is full" : "Waiting for people to join"}
              </div>
              <div>
                Players in the lobby: {lobby.users.length}/{lobby.maxPlayers}
              </div>
              <br />
              <UsersInLobby
                lobby={lobby}
                handleRemoveUserFromLobby={handleRemoveUserFromLobby}
              />
              <br />
            </Col>
            <Col>
              <Form>
                <Form.Group key="stacked-radio">
                  <Form.Label>Class</Form.Label> <br />
                  <Form.Check
                    className="mx-3"
                    label="🌫️ None"
                    name="class"
                    type="radio"
                    defaultChecked
                    onChange={(e) => setCharacter(null)}
                  />
                  <Form.Check
                    className="mx-3"
                    label="🗡️ Rogue"
                    name="class"
                    type="radio"
                    disabled={
                      charactersTaken.includes("rogue") || character === "rogue"
                    }
                    onChange={(e) => setCharacter("rogue")}
                  />
                  <Form.Check
                    className="mx-3"
                    label="🛡 Warrior"
                    name="class"
                    type="radio"
                    disabled={
                      charactersTaken.includes("warrior") ||
                      character === "warrior"
                    }
                    onChange={(e) => setCharacter("warrior")}
                  />
                  <Form.Check
                    className="mx-3"
                    label="🧙 Wizard"
                    name="class"
                    type="radio"
                    disabled={
                      charactersTaken.includes("wizard") ||
                      character === "wizard"
                    }
                    onChange={(e) => setCharacter("wizard")}
                  />
                  <Form.Check
                    className="mx-3"
                    label="🏹 Ranger"
                    name="class"
                    type="radio"
                    disabled={
                      charactersTaken.includes("ranger") ||
                      character === "ranger"
                    }
                    onChange={(e) => setCharacter("ranger")}
                  />
                </Form.Group>
                <Form.Group className="mb-1">
                  <Form.Label>Variant</Form.Label> <br />
                  <Form.Check
                    className="mx-3"
                    label="♂ Male"
                    name="gender"
                    type="radio"
                    defaultChecked
                    onChange={(e) => setGender("male")}
                  />
                  <Form.Check
                    className="mx-3"
                    label="♀ Female"
                    name="gender"
                    type="radio"
                    onChange={(e) => setGender("female")}
                  />
                </Form.Group>
              </Form>
            </Col>
          </Row>
          <Row>
            {lobby.users.length > 1 && user.username === lobby.host.username ? (
              <Button type="submit" classType="mx-auto" onClick={createGame}>
                Start Game
              </Button>
            ) : (
              ""
            )}
          </Row>
        </>
      )}
    </>
  );
}

Lobby.propTypes = {
  User: PropTypes.shape({
    username: PropTypes.string.isRequired,
  }),

  users: PropTypes.arrayOf(PropTypes.User),
};
